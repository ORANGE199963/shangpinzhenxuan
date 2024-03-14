package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductService {

    public List<ProductSku> findIndexSkuList();


    PageInfo findByPage(Integer pageNum, Integer pageSize, ProductSkuDto productSkuDto);

    List<Brand> findAllBrand();

    ProductItemVo item(Long skuId);

    ProductSku getBySkuId(Long skuId);

    void updateSkuStockNumAndSaleNum(List<SkuSaleDto> skuSaleDtoList);
}
