package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {
    void insertProductDetails(ProductDetails productDetails);

    ProductDetails getByProductId(Long productId);

    void deleteByProductId(Long id);
}
