package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.product.feign.ProductServiceFeign;
import com.atguigu.spzx.product.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ProductServiceFeign productServiceFeign;

    @Override
    public void addToCart(Long skuId, Integer num) {
        String key = "user:cart:" + ThreadLocalUtil.getUserInfo().getId();

        String jsonString = (String)stringRedisTemplate.opsForHash().get(key, String.valueOf(skuId));//大key  小key , 返回值就是一个sku对应的CartInfo的json字符串

        CartInfo cartInfo = JSON.parseObject(jsonString, CartInfo.class);

        //远程服务调用
        ProductSku productSku = productServiceFeign.getProductId(skuId);

        //新商品加入到购物车
        if (cartInfo==null){
            cartInfo = new CartInfo();
            cartInfo.setUserId(ThreadLocalUtil.getUserInfo().getId());
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(productSku.getSalePrice());//sku的单价（在当前购物车服务中，去调用商品服务，根据skuid，返回sku商品对象）
            cartInfo.setSkuNum(num);//商品数量
            cartInfo.setImgUrl(productSku.getThumbImg());//sku图片（轮播图）
            cartInfo.setSkuName(productSku.getSkuName());//sku的商品名称
            cartInfo.setIsChecked(1);//默认选中状态


            jsonString = JSON.toJSONString(cartInfo);//小value
            stringRedisTemplate.opsForHash().put(key,String.valueOf(skuId),jsonString);
        }else {
            //该商品以被加入购物车，只需要修改skunum数量即可
            cartInfo.setSkuNum(cartInfo.getSkuNum() + num);

            jsonString = JSON.toJSONString(cartInfo);//小value
            stringRedisTemplate.opsForHash().put(key,String.valueOf(skuId),jsonString);
        }
    }

    @Override
    public List<CartInfo> findCartList() {
        Long id = ThreadLocalUtil.getUserInfo().getId();
        String key = "user:cart:" + id;
        List<Object> values = stringRedisTemplate.opsForHash().values(key);

          List<CartInfo> cartInfoList  = values.stream().map(obj -> {
              String jsonString  = String.valueOf(obj);
              CartInfo cartInfo = JSON.parseObject(jsonString,CartInfo.class);
              return cartInfo;
          }).collect(Collectors.toList());
        return cartInfoList                                                                                                                 ;
    }

    @Override
    public void deleteCart(Long skuId) {
        Long id = ThreadLocalUtil.getUserInfo().getId();
        String bigKey = "user:cart:" + id;

        String smallKey = String.valueOf(skuId);
        stringRedisTemplate.opsForHash().delete(bigKey,smallKey);
    }

    @Override
    public void updateChecked(Long skuId, Integer isChecked) {
        Long id = ThreadLocalUtil.getUserInfo().getId();
        String bigKey = "user:cart:" + id;
        String smallKey = String.valueOf(skuId);
        Object o = stringRedisTemplate.opsForHash().get(bigKey, smallKey);
        String json = String.valueOf(o);
        CartInfo cartInfo = JSON.parseObject(json,CartInfo.class);
        cartInfo.setIsChecked(isChecked);

        stringRedisTemplate.opsForHash().put(bigKey,smallKey,JSON.toJSONString(cartInfo));



    }

    @Override
    public void allChecked(Integer isChecked) {
        List<CartInfo> cartList = this.findCartList();

        Long id = ThreadLocalUtil.getUserInfo().getId();
        String bigKey = "user:cart:" + id;

        cartList.forEach(cartInfo -> {
            cartInfo.setIsChecked(isChecked);
            String smallKey = String.valueOf(cartInfo.getSkuId());

            stringRedisTemplate.opsForHash().put(bigKey,smallKey,JSON.toJSONString(cartInfo));

        });
    }

    @Override
    public void clearCart() {
        Long id = ThreadLocalUtil.getUserInfo().getId();
        String bigKey = "user:cart:" + id;

        stringRedisTemplate.delete(bigKey);
    }

    @Override
    public List<CartInfo> getCartInfoIsCheckedOne() {
        List<CartInfo> all = this.findCartList();
        List<CartInfo> collectOne = all.stream().filter(cartInfo -> cartInfo.getIsChecked().intValue() == 1).collect(Collectors.toList());

        return collectOne;
    }
}
