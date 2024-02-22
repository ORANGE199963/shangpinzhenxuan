package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageInfo;

public interface SysRoleService {
    PageInfo findByPage(Integer pageNum, Integer pageSize, SysRoleDto sysRoleDto);

    void deleteById(Long roleId);

    void addRole(SysRole sysRole);
}
