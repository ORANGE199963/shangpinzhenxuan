package com.atguigu.spzx.order.feign;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order")
public interface OrderServiceFeign {

    @GetMapping("/api/order/orderInfo/getByOrderNo/{orderNo}")
    public OrderInfo getByOrderNo(@PathVariable String orderNo);

    @GetMapping("/api/order/orderInfo/afterPaySuccess/{orderNo}/{payType}")
    public Result afterPaySuccess(@PathVariable String orderNo, @PathVariable Integer payType);
}
