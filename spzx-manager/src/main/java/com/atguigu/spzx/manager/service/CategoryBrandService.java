package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryBrandService {
    PageInfo findByPage(Integer pageNum, Integer pageSize, CategoryBrandDto categoryBrandDto);

    void addCategoryBrand(CategoryBrand categoryBrand);

    List<Long> getIdsByCategory3Id(Long category3Id);

    void updateById(CategoryBrand categoryBrand);

    void deleteById(Long id);
}
