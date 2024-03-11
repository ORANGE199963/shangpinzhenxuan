package com.atguigu.spzx.product.feign;

import com.atguigu.spzx.model.entity.product.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-product")
public interface ProductServiceFeign {

    @GetMapping("/api/product/getProductId/{skuId}")
    public ProductSku getProductId(@PathVariable Long skuId);
}
