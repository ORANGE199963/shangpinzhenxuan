package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.entity.product.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<Category> findByParentId(Long parentId);

    List<Category> findAll();

    void importData(MultipartFile file);
}
