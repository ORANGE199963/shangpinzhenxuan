package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductSkuMapper {

    void addProductSku(ProductSku productSku);

    List<ProductSku> getByProductId(Long productId);

    void deleteByProductId(Long id);

    void updateStatus(@Param("spuId") Long spuId, @Param("status") Integer status);

    ProductSku getById(Long skuId);

    void updateStatusBySkuId(@Param("skuId") Long skuId,@Param("status") Integer status);

    void updateById(ProductSku productSku);
}