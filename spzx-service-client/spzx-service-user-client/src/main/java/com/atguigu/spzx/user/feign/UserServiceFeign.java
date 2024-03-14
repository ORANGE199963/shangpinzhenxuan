package com.atguigu.spzx.user.feign;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-user")
public interface UserServiceFeign {

    @GetMapping("/api/user/findByAddressId/{addressId}")
    public UserAddress findByAddressId(@PathVariable Long addressId);

    @GetMapping("/api/user/getByUserId/{userId}")
    public UserInfo getByUserId(@PathVariable Long userId);
}
