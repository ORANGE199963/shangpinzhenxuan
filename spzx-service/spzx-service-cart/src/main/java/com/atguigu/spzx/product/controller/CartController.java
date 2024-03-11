package com.atguigu.spzx.product.controller;


import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.product.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车接口管理")
@RestController
@RequestMapping(value="/api/order/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("getCartInfoIsCheckedOne")
    public List<CartInfo> getCartInfoIsCheckedOne(){
        return cartService.getCartInfoIsCheckedOne();
    }

    @GetMapping("auth/clearCart")
    public Result clearCart(){
        cartService.clearCart();
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("auth/allCheckCart/{isChecked}")
    public Result allChecked(@PathVariable Integer isChecked){
        cartService.allChecked(isChecked);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("auth/checkCart/{skuId}/{isChecked}")
    public Result updateChecked(@PathVariable Long skuId,@PathVariable Integer isChecked){
        cartService.updateChecked(skuId,isChecked);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @DeleteMapping("auth/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable Long skuId){
        cartService.deleteCart(skuId);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/auth/cartList")
    public Result findCartList(){
        List<CartInfo> list = cartService.findCartList();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/auth/addToCart/{skuId}/{num}")
    public Result addToCart(@PathVariable Long skuId,@PathVariable Integer num){
        cartService.addToCart(skuId,num);
        return Result.build(num, ResultCodeEnum.SUCCESS);
    }
}
