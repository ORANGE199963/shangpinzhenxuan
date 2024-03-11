package com.atguigu.spzx.order.controller;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.atguigu.spzx.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "分类接口管理")
@RestController
@RequestMapping(value = "/api/order/orderInfo")

public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/auth/trade")
    public Result trade(){
        TradeVo tradeVo = orderService.trade();
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }
}
