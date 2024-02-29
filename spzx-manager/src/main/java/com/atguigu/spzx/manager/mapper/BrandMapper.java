package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// com.atguigu.spzx.manager.mapper
@Mapper
public interface BrandMapper {
    
    public abstract List<Brand> findByPage();

    void save(Brand brand);

    public abstract void updateById(Brand brand);

    public abstract void deleteById(Long id);

    List<Brand> findAll();
}