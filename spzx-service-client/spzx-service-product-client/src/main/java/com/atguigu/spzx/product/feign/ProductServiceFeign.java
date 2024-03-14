package com.atguigu.spzx.product.feign;

import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-product")
public interface ProductServiceFeign {

    @GetMapping("/api/product/getProductId/{skuId}")
    public ProductSku getProductId(@PathVariable Long skuId);


    @PostMapping("/api/product/updateSkuStockNumAndSaleNum")
    public Result updateSkuStockNumAndSaleNum(@RequestBody List<SkuSaleDto> skuSaleDtoList);
}
