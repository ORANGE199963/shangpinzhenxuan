package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.atguigu.spzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> selectList(ProductDto productDto);

    List<ProductUnit> findAllProductUnit();

    void addProduct(Product product);

    Product getById(Long productId);

    void deleteById(Long id);

    void updateAuditStatus(@Param("productId") Long productId,@Param("auditStatus") Integer auditStatus);
}
