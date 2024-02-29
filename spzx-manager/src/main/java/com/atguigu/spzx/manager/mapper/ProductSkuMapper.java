package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper {

    void addProductSku(ProductSku productSku);

    List<ProductSku> getByProductId(Long productId);

    void deleteByProductId(Long id);
}