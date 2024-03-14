package com.atguigu.spzx.order.controller;

import com.atguigu.spzx.model.dto.h5.OrderInfoDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.atguigu.spzx.order.service.OrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "分类接口管理")
@RestController
@RequestMapping(value = "/api/order/orderInfo")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("auth/cancelOrder/{orderNo}/{reason}")
    public Result cancelOrder(@PathVariable String orderNo,@PathVariable String reason){
        orderService.cancelOrder(orderNo,reason);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //支付成功后，调用订单服务的接口，修改订单的
    @GetMapping("afterPaySuccess/{orderNo}/{payType}")
    public Result afterPaySuccess(@PathVariable String orderNo,@PathVariable Integer payType){
//        UPDATE `order_info` SET pay_type = 2,order_status=1 , payment_time = NOW() WHERE order_no = #{orderNo}
        orderService.afterPaySuccess(orderNo,payType);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //支付服务（确认支付）调用这个接口
    @GetMapping("getByOrderNo/{orderNo}")
    public OrderInfo getByOrderNo(@PathVariable String orderNo){
        return orderService.getByOrderNo(orderNo);
    }

    /**
     * 立即购买接口
     * @param skuId
     * @return
     */
    @GetMapping("/auth/buy/{skuId}")
    public Result buy(@PathVariable Long skuId){
        TradeVo tradeVo = orderService.buy(skuId);
        return Result.build(tradeVo,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/auth/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable Integer pageNum,
                           @PathVariable Integer pageSize,
                           @RequestParam(required = false) Integer orderStatus){

        PageInfo pageInfo = orderService.findPage(pageNum,pageSize,orderStatus);
        return Result.build(pageInfo,ResultCodeEnum.SUCCESS);
    }
    @GetMapping("auth/{orderId}")
    public Result getByOrderId(@PathVariable Long orderId){
        //根据订单id查询某个订单，并且将其订单明细集合一同查询到
        OrderInfo orderInfo = orderService.getByOrderId(orderId);
        return Result.build(orderInfo,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("auth/submitOrder")
    public Result submitOrder( @RequestBody OrderInfoDto orderInfoDto){
        Long orderId = orderService.submitOrder(orderInfoDto);
        return Result.build(orderId, ResultCodeEnum.SUCCESS);
    }
    @GetMapping("/auth/trade")
    public Result trade(){
        TradeVo tradeVo = orderService.trade();
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }
}
