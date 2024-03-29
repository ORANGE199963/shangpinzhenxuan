package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {


    List<Category> findByParentId(Long parentId);

    Integer countByParentId(Long parentId);

    List<Category> findAll();

    void addCategory(List<Category> categoryList);

    Category selectById(Long parentId);
}
