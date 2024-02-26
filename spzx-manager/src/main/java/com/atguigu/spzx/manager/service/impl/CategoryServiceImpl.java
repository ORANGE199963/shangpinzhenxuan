package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public List<Category> findByParentId(Long parentId) {

        List<Category> list = categoryMapper.findByParentId(parentId);
        list.forEach(category -> {
//            Integer count = categoryMapper.countByParentId(category.getId());
            category.setHasChildren(category.getChildrenCount()>0);
        });
        return list;
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
}
