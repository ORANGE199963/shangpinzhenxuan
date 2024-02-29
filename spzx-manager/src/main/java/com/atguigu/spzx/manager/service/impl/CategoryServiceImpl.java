package com.atguigu.spzx.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.manager.excel.CategoryReadListener;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CategoryReadListener categoryReadListener;
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

    @Override
    public void importData(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, CategoryExcelVo.class,categoryReadListener)
                    .sheet("新商品分类")
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
