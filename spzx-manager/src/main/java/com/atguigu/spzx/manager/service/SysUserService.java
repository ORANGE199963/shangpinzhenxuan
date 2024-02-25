package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.manager.controller.SysUserVo;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SysUserService {
    LoginVo login(LoginDto loginDto);

    SysUserVo getUserInfo(String token);

    void logout(String token);

    PageInfo findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    void addUser(SysUser sysUser);

    void editUser(SysUser sysUser);

    void deleteById(Long id);

    void batchDelete(List<Long> ids);
}
