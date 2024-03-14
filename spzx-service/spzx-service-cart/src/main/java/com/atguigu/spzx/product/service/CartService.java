package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.h5.CartInfo;

import java.util.List;

public interface CartService {


    void addToCart(Long skuId, Integer num);

    List<CartInfo> findCartList();

    void deleteCart(Long skuId);

    void updateChecked(Long skuId, Integer isChecked);

    void allChecked(Integer isChecked);

    void clearCart();

    List<CartInfo> getCartInfoIsCheckedOne();

    void deleteCartInfoIsCheckedOne();
}
