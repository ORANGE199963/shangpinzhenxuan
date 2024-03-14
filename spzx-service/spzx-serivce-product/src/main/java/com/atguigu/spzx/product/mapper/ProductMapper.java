package com.atguigu.spzx.product.mapper;


import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductSku> findIndexSkuList();

    List<ProductSku> findByPage(ProductSkuDto productSkuDto);

    List<Brand> findAllBrand();

    ProductSku getSkuBySkuId(Long skuId);

    Product getSpuBySpuId(Long productId);

    ProductDetails getProductDetailsByProductId(Long productId);

    List<ProductSku> getSkuListByProductId(Long productId);

    void updateSku(SkuSaleDto skuSaleDto);
}
