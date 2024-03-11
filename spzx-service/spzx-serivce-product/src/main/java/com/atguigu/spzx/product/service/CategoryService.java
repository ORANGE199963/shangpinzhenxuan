package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findByParentId(Long id);

    List<Category> findCategoryTree();
}
