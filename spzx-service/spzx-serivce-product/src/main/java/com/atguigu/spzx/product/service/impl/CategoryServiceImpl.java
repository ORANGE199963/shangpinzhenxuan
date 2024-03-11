package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public List<Category> findByParentId(Long id) {
        return categoryMapper.findByParentId(id);
    }

    @Cacheable(value = "category",key = "'findCategoryTree'")
    @Override
    public List<Category> findCategoryTree() {
//        String jsonArrayString = stringRedisTemplate.opsForValue().get("categorytree");
//        List<Category> categoryList = JSON.parseArray(jsonArrayString, Category.class);
//        if (categoryList!=null && categoryList.size()>0){
//            return categoryList;
//        }

        List<Category> all = categoryMapper.findAll();

        List<Category> oneCategoryList = all.stream().filter(category -> category.getParentId().longValue()==0).collect(Collectors.toList());

        oneCategoryList.stream().forEach(oneCategory -> {


            List<Category> twoCategoryList = all.stream().filter(category -> category.getParentId().longValue()==oneCategory.getId().longValue()).collect(Collectors.toList());

            twoCategoryList.forEach(twoCategory -> {
                List<Category> threeCategoryList = all.stream().filter(category -> category.getParentId().longValue()==twoCategory.getId().longValue()).collect(Collectors.toList());
                twoCategory.setChildren(threeCategoryList);
            });

            oneCategory.setChildren(twoCategoryList);



        });


//        stringRedisTemplate.opsForValue().set("categorytree", JSON.toJSONString(oneCategoryList),30, TimeUnit.MINUTES);
        return oneCategoryList;
    }
}
