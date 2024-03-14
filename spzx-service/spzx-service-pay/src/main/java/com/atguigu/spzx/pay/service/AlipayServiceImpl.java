package com.atguigu.spzx.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.order.feign.OrderServiceFeign;
import com.atguigu.spzx.pay.mapper.PaymentInfoMapper;
import com.atguigu.spzx.pay.properties.AlipayProperties;
import com.atguigu.spzx.product.feign.ProductServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    OrderServiceFeign orderServiceFeign;

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Autowired
    AlipayProperties alipayProperties;

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    ProductServiceFeign productServiceFeign;

    @Override
    public String alipay(String orderNo) {

        Long userId = ThreadLocalUtil.getUserInfo().getId();

        //当前支付服务，调用订单服务，根据订单编号，查询订单对象
        OrderInfo orderInfo = orderServiceFeign.getByOrderNo(orderNo);

        //1、创建该订单对应的一个支付记录paymentInfo，每个订单最多只有一个支付记录
        this.createPaymentInfo(userId, orderNo, orderInfo);

        //2、调用支付宝的 手机网站支付接口2.0 , 返回form表单字符串
        String form = this.handleAlipayWapPay2(orderInfo);

        return form;
    }

    @Override
    public void afterPaySuccess(Map<String, String> paramsMap) {
        //1、调用订单服务，修改订单信息（支付方式，支付时间，支付状态）
        String orderNo = paramsMap.get("out_trade_no");//曾经调用支付宝的第一个接口时，传递的订单编号
        orderServiceFeign.afterPaySuccess(orderNo,2);//2：支付宝支付

        //2、修改支付记录（当前支付服务自己来修改），
//        UPDATE `payment_info`
//        SET out_trade_no = ?,   支付宝返回的交易流水号（map.get("trade_no")）
//        payment_status =  1 ,  支付状态
//                callback_time = NOW() ,  支付成功的时间（回调时间）
//                callback_content = ?     支付宝返回的所有的数据，就是回调接口的参数map（回调内容）  map.toString()
//        WHERE order_no = ?  //订单编号

        String trade_no = paramsMap.get("trade_no");//支付宝返回的交易流水号（凭证号）
        paymentInfoMapper.afterPaySuccess(trade_no,orderNo,paramsMap.toString());

        //3、修改sku的库存和销量，根据orderNo查询到List<OrderItem> （订单服务），每个OrderItem对应sku，进行库存和销量的修改（商品服务）
        OrderInfo orderInfo = orderServiceFeign.getByOrderNo(orderNo);
        List<OrderItem> orderItemList = orderInfo.getOrderItemList();//订单明细列表

        List<SkuSaleDto> skuSaleDtoList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            Long skuId = orderItem.getSkuId();
            Integer skuNum = orderItem.getSkuNum();
            //UPDATE `product_sku` SET stock_num = stock_num - ?   , sale_num=sale_num  + ? WHERE id = ?
            SkuSaleDto skuSaleDto = new SkuSaleDto();

            skuSaleDto.setSkuId(skuId);
            skuSaleDto.setNum(skuNum);

            skuSaleDtoList.add(skuSaleDto);
        }

        //去调用的商品服务，skuSaleDtoList传过去
        productServiceFeign.updateSkuStockNumAndSaleNum(skuSaleDtoList);

    }

    private String handleAlipayWapPay2(OrderInfo orderInfo) {

        //无论调用支付宝的那个接口，都是要使用到一个AlipayClient对象（单例即可）
//        AlipayClient alipayClient = new DefaultAlipayClient(
//                alipayProperties.getAlipay_url(),
//                alipayProperties.getApp_id(),
//                alipayProperties.getApp_private_key(),
//                AlipayProperties.format,//参数的格式json
//                AlipayProperties.charset,
//                alipayProperties.getAlipay_public_key(),
//                AlipayProperties.sign_type);//非对称的加密算法


        //对于支付端不同的接口，使用不同的request对象
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();


        //异步接收地址，仅支持http/https，公网可访问（当支付成功或失败时，该接口会被自动调用）
        request.setNotifyUrl(alipayProperties.getNotify_payment_url());

        //同步跳转地址，仅支持http/https （支付成功后，跳转的页面地址）
        request.setReturnUrl(alipayProperties.getReturn_payment_url());


        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        //商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no",orderInfo.getOrderNo() );//每次去调用支付宝接口时，需要传递的一个具有唯一性支付编号（可以使用订单的编号）
        //支付金额，最小值0.01元
        bizContent.put("total_amount", orderInfo.getTotalAmount().doubleValue());
        //订单标题，不可使用特殊符号（随便写）
        bizContent.put("subject", "测试商品");//手机端支付时会显示
        //手机网站支付，固定写QUICK_WAP_WAY
        bizContent.put("product_code", "QUICK_WAP_WAY");


        request.setBizContent(bizContent.toString());//必传参数，是json字符串格式

        //发起一个post类型的请求，对支付宝接口进行调用
        AlipayTradeWapPayResponse response = null;
        try {
            response = alipayClient.pageExecute(request, "POST");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }


        //支付宝返回的form表单字符串
        String pageRedirectionData = response.getBody();

        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }


        return pageRedirectionData;
    }

    private void createPaymentInfo(Long userId, String orderNo, OrderInfo orderInfo) {

        //1、判断当前订单是否存在支付记录
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);
        //如果该订单已存在支付记录，直接返回即可，不需要重复创建
        if (paymentInfo != null) {
            return;
        }

        //2、如果该订单不存在支付记录，new一个对象，逐个set赋值
        paymentInfo = new PaymentInfo();


        paymentInfo.setUserId(userId);
        paymentInfo.setOrderNo(orderNo);//订单编号
        paymentInfo.setPayType(2);//支付方式 1-微信 2-支付宝
        paymentInfo.setAmount(orderInfo.getTotalAmount());//支付金额，就是订单中的total_amount； 需要在当前支付服务中去调用订单服务根据orderNo订单编号，查询订单
        String content = "";
        for (OrderItem orderItem : orderInfo.getOrderItemList()) {
            content += orderItem.getSkuName() + " ";
        }
        paymentInfo.setContent(content);//sku1name + sku2name... 交易内容
        paymentInfo.setPaymentStatus(0);//未支付

//        paymentInfo.setOutTradeNo(); 这个字段暂时不需要赋值（支付成功后，支付宝端有一个交易流水号，赋值给OutTradeNo）
//        paymentInfo.setCallbackTime(); 记录支付成功的时间
//        paymentInfo.setCallbackContent(); 记录成功后，将支付宝返回的所有的数据，暂存到这个字段

        //思考：支付成功后，关于这个支付记录要做什么操作？
        // 1、支付记录中的out_trade_no，要存储支付宝返回的流水号（支付成功后支付会返回）
        // 2、payment_status支付记录的支付状态，改成1已支付
        // 3、callback_time 支付成功的时间（回调时间）
        // 4、callback_content  支付成功后，支付宝返回一些数据，将这些数据存储到content中进行备份

        paymentInfoMapper.addPaymentInfo(paymentInfo);
    }
}
