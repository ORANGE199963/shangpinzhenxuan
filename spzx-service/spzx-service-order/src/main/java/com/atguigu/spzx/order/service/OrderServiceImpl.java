package com.atguigu.spzx.order.service;

import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.cart.feign.CartServiceFeign;
import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.model.dto.h5.OrderInfoDto;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.order.OrderLog;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.atguigu.spzx.order.mapper.OrderMapper;
import com.atguigu.spzx.pay.feign.PayServiceFeign;
import com.atguigu.spzx.product.feign.ProductServiceFeign;
import com.atguigu.spzx.user.feign.UserServiceFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartServiceFeign cartServiceFeign;

    @Autowired
    UserServiceFeign userServiceFeign;

    @Autowired
    ProductServiceFeign productServiceFeign;

    @Autowired
    PayServiceFeign payServiceFeign;
    @Autowired
    OrderMapper orderMapper;


    @Override
    public TradeVo trade() {

        //远程调用,返回isChecked=1的CartInfo
        List<CartInfo> cartInfoList = cartServiceFeign.getCartInfoIsCheckedOne();

        //CarInfo（购物车中的每个sku商品对应一个CartInfo对象）   OrderItem（每个CartInfo（isChecked=1）对应一个订单明细OrderItem对象）


        //每个CartInfo，创建一个OrderItem订单明细，暂时不需要存储到数据库中，只是返回给前端做确认
        List<OrderItem> orderItemList = cartInfoList.stream().map(cartInfo -> {
            OrderItem orderItem = new OrderItem();
//            orderItem.setOrderId();当前订单明细对应的订单的id（一个订单对应多个订单明细）
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setSkuNum(cartInfo.getSkuNum());

            return orderItem;
        }).collect(Collectors.toList());


        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList(orderItemList);//给前端返回订单明细
        tradeVo.setTotalAmount(this.calTotalAmount(orderItemList));//订单明细总金额


        return tradeVo;
    }
    @Transactional
    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {

        //1、创建订单
        UserInfo userInfo = ThreadLocalUtil.getUserInfo();
        Long orderInfoId = this.createOrderInfo(userInfo,orderInfoDto);

        //2、添加多个订单明细，每个订单对应一个sku商品，需要进行库存的校验
        this.createOrderItem(orderInfoDto,orderInfoId);

        //3、添加订单日志
        this.createOrderLog(orderInfoId);

        if (orderInfoDto.getFlag().intValue()==1) {
            //4、删除购物车中的is_checked=1 的商品
            cartServiceFeign.deleteCartInfoIsCheckedOne();
        }

        return orderInfoId;
    }

    @Override
    public OrderInfo getByOrderId(Long orderId) {
        OrderInfo orderInfo = orderMapper.getOrderInfoById(orderId);
        List<OrderItem> orderItemList = orderMapper.getOrderItemListByOrderId(orderId);

        orderInfo.setOrderItemList(orderItemList);

        return orderInfo;
    }

    @Override
    public PageInfo findPage(Integer pageNum, Integer pageSize, Integer orderStatus) {
        //1、用户id
        Long userId = ThreadLocalUtil.getUserInfo().getId();

        //2、设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //3、条件查询  where user_id =? and  order_status = ?
        List<OrderInfo> list = orderMapper.findList(orderStatus,userId);

        //4、将每个订单的订单明细一同查询出来
        for (OrderInfo orderInfo : list) {
            //当前订单的订单明细集合
            List<OrderItem> orderItemList = orderMapper.getOrderItemListByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItemList);
        }

        return new PageInfo(list);
    }

    @Override
    public TradeVo buy(Long skuId) {
        //立即购买，也需要创建一个订单明细集合，只是该集合中只需要一个订单明细即可

        //1、根据skuId，当前订单去调用商品服务，查询sku商品对象
        ProductSku productSku = productServiceFeign.getProductId(skuId);

        //2、利用这一个sku商品对象，封装成一个订单明细，存储在list集合中
        OrderItem orderItem = new OrderItem();

        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItem.setSkuNum(1);//立即购买时，商品数量只有一个

        //3、封装tradeVo
        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList(Arrays.asList(orderItem));
        tradeVo.setTotalAmount(productSku.getSalePrice());

        return tradeVo;
    }

    @Override
    public void afterPaySuccess(String orderNo, Integer payType) {

        //1、当前订单服务中，从orderInfo中获取userId
        Long userId = orderMapper.getByOrderNo(orderNo).getUserId();
        //2、调用用户服务，根据userId返回userInfo
        UserInfo userInfo = userServiceFeign.getByUserId(userId);

        //3、创建订单日志，日志中的username不能从threadLocal去获取
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderMapper.getByOrderNo(orderNo).getId());
        orderLog.setNote("订单已支付");
        orderLog.setProcessStatus(1);//订单状态
        orderLog.setOperateUser(userInfo.getUsername());//当前用户的username字段
        orderMapper.addOrderLog(orderLog);


        orderMapper.afterPaySuccess(orderNo,payType);
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        //查询订单
        OrderInfo orderInfo = orderMapper.getByOrderNo(orderNo);
        //查询这个订单的订单明细列表
        orderInfo.setOrderItemList(orderMapper.getOrderItemListByOrderId(orderInfo.getId()));//该订单的订单明细列表
        return orderInfo;
    }

    @Override
    public void cancelOrder(String orderNo, String reason) {
        //1、修改订单的状态改成-1,赋值取消理由reason
        orderMapper.cancelOrder(orderNo,reason);


        //2、取消订单时，创建订单日志
        OrderInfo orderInfo = orderMapper.getByOrderNo(orderNo);
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setNote("取消订单");
        orderLog.setOperateUser(ThreadLocalUtil.getUserInfo().getUsername());
        orderLog.setProcessStatus(-1);
        orderMapper.addOrderLog(orderLog);

        //3、调用支付服务（退款+修改支付记录）
        payServiceFeign.cancelPayment(orderNo);


        //4、调用商品服务
        List<SkuSaleDto> list = new ArrayList<>();
        List<OrderItem> orderItemList = orderMapper.getOrderItemListByOrderId(orderInfo.getId());

        for (OrderItem orderItem : orderItemList) {
            Long skuId = orderItem.getSkuId();
            Integer skuNum = 0 - orderItem.getSkuNum().intValue();

            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(skuId);
            skuSaleDto.setNum(skuNum);

            list.add(skuSaleDto);
        }

        productServiceFeign.updateSkuStockNumAndSaleNum(list);

    }


    private void createOrderLog(Long orderInfoId) {
        OrderLog orderLog = new OrderLog();

        orderLog.setOrderId(orderInfoId);
        String username = ThreadLocalUtil.getUserInfo().getUsername();
        orderLog.setOperateUser(username);//当前用户名
        orderLog.setProcessStatus(0);//订单状态 0：未支付 ， 1：已支付（等待发货）
        orderLog.setNote("提交订单");// 提交订单 、  支付订单 、 取消订单....  订单的不同阶段（状态）的描述

        orderMapper.addOrderLog(orderLog);
    }

    private void createOrderItem(OrderInfoDto orderInfoDto,Long orderInfoId) {

        //前端传递的订单明细集合
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();

        //遍历订单明细集合，每个订单明细都需要进行库存的校验，并且每个订单明细需要为订单id字段赋值
        for (OrderItem orderItem : orderItemList) {
            Long skuId = orderItem.getSkuId();
            Integer skuNum = orderItem.getSkuNum();

            //根据skuid查询当前sku的剩余库存和skuNum比较
            //当前订单服务若想查询某个sku，就去调用商品服务，根据skuId返回sku对象
            ProductSku productSku = productServiceFeign.getProductId(skuId);
            if (productSku.getStockNum().intValue() < skuNum.intValue()) {
                //当前sku商品库存不足
                throw new GuiguException(ResultCodeEnum.STOCK_LESS);
            }

            orderItem.setOrderId(orderInfoId);

            orderMapper.addOrderItem(orderItem);//添加订单明细
        }
    }

    private Long createOrderInfo(UserInfo userInfo,OrderInfoDto orderInfoDto) {
        //1、创建OrderInfo对象
        OrderInfo orderInfo = new OrderInfo();


        orderInfo.setUserId(userInfo.getId());//当前订单所属的用户id
        orderInfo.setNickName(userInfo.getNickName());//当前用户的昵称
        orderInfo.setOrderNo(System.currentTimeMillis() + "");//订单编号（唯一性）

        //优惠券（暂未使用）
        orderInfo.setCouponId(0L);
        orderInfo.setCouponAmount(new BigDecimal(0));

        //计算总金额
        BigDecimal bigDecimal = this.calTotalAmount(orderInfoDto.getOrderItemList());
        orderInfo.setTotalAmount(bigDecimal);//订单总金额
        orderInfo.setOriginalTotalAmount(bigDecimal.add(orderInfoDto.getFeightFee()));//由于没有使用优惠券并且免邮费
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());//免邮费
        orderInfo.setOrderStatus(0);//【0->待付款；1->待发货；2->已发货；3->待用户收货，已完成；-1->已取消】   刚刚创建的订单，状态0

        //当前订单需要保存收货地址信息
        Long userAddressId = orderInfoDto.getUserAddressId();//地址id
        UserAddress userAddress = userServiceFeign.findByAddressId(userAddressId);

        orderInfo.setReceiverName(userAddress.getName());//收件人名称
        orderInfo.setReceiverPhone(userAddress.getPhone());//收件人手机号
        orderInfo.setReceiverTagName(userAddress.getTagName());//收货地址别名
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());//省份
        orderInfo.setReceiverCity(userAddress.getCityCode());//城市
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());//区
        orderInfo.setReceiverAddress(userAddress.getFullAddress());//详细地址

//        orderInfo.setPayType();//支付方式
//        orderInfo.setPaymentTime();//支付时间，这两个字段，等支付成功后再来赋值

        orderInfo.setRemark(orderInfoDto.getRemark());//订单备注

        //调用orderMapper中的添加，将当前orderInfo添加到数据库中，并且返回当前订单的id，返回订单id给前端，并且每个订单明细需要设置orderId字段
        orderMapper.addOrder(orderInfo);

        return orderInfo.getId();
    }

    private BigDecimal calTotalAmount(List<OrderItem> orderItemList) {
        BigDecimal totalAomunt = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            Integer skuNum = orderItem.getSkuNum();
            BigDecimal skuPrice = orderItem.getSkuPrice();

            BigDecimal sum = skuPrice.multiply((new BigDecimal(skuNum)));
            totalAomunt = totalAomunt.add(sum);
        }
        return totalAomunt;
    }
}
