package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {
    public List<SysUser> findAll();

    SysUser findByUsername(String userName);
}