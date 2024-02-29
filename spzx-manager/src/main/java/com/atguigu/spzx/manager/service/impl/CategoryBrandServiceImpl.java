package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.manager.mapper.CategoryBrandMapper;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryBrandService;
import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {

    @Autowired
    CategoryBrandMapper categoryBrandMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, CategoryBrandDto categoryBrandDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<CategoryBrand> list = categoryBrandMapper.selectList(categoryBrandDto);

        return new PageInfo(list);
    }

    @Override
    public void addCategoryBrand(CategoryBrand categoryBrand) {
        Integer count  = categoryBrandMapper.selectByCategoryIdAndBrandId(categoryBrand);
        if(count>0){
            throw new GuiguException(ResultCodeEnum.CATEGORY_BRAND_EXISTS);
        }

        categoryBrandMapper.addCategoryBrand(categoryBrand);
    }

    @Override
    public List<Long> getIdsByCategory3Id(Long category3Id) {
        Category category3 = categoryMapper.selectById(category3Id);
        Category category2 = categoryMapper.selectById(category3.getParentId());

        List<Long> idList = new ArrayList<>();
        idList.add(category2.getParentId());
        idList.add(category3.getParentId());
        idList.add(category3Id);
        return idList;
    }

    @Override
    public void updateById(CategoryBrand categoryBrand) {
        categoryBrandMapper.updateById(categoryBrand) ;
    }

    @Override
    public void deleteById(Long id) {
        categoryBrandMapper.deleteById(id) ;
    }
}
