package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.OrderStaService;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/order/sta")
public class OrderStaController {

    @Autowired
    OrderStaService orderStaService;

    @GetMapping("getOrderSta")
    public Result getOrderSta(OrderStatisticsDto orderStatisticsDto){
        Map map = orderStaService.getOrderSta(orderStatisticsDto);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }
}
