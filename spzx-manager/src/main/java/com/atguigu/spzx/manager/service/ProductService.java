package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.atguigu.spzx.model.entity.product.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {
    PageInfo findByPage(Integer pageNum, Integer pageSize, ProductDto productDto);

    List<ProductUnit> findAllProductUnit();

    void addProduct(Product product);

    Product getByProductId(Long productId);

    void deleteById(Long id);

    void updateAuditStatus(Long productId, Integer auditStatus);

    void updateStatus(Long spuId, Integer status);

    void updateSkuStatus(Long skuId, Integer status);

    void updateProduct(Product product);
}
