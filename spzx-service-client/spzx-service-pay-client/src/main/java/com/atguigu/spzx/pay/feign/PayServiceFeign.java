package com.atguigu.spzx.pay.feign;

import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-pay")
public interface PayServiceFeign {

    @GetMapping("api/order/alipay/cancelPayment/{orderNo}")
    public Result cancelPayment(@PathVariable String orderNo);
}
