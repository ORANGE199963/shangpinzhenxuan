package com.atguigu.spzx.cart.feign;

import com.atguigu.spzx.model.entity.h5.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-cart")
public interface CartServiceFeign {
    @GetMapping("/api/order/cart/getCartInfoIsCheckedOne")
    public List<CartInfo> getCartInfoIsCheckedOne();

    @GetMapping("/api/order/cart/deleteCartInfoIsCheckedOne")
    public void deleteCartInfoIsCheckedOne();
}
