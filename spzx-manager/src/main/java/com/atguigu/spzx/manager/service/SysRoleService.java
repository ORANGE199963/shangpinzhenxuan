package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SysRoleService {
    PageInfo findByPage(Integer pageNum, Integer pageSize, SysRoleDto sysRoleDto);

    void deleteById(Long roleId);

    void addRole(SysRole sysRole);

    void updateRole(SysRole sysRole);

    List<SysRole> findRoleList();

    void doAssign(AssginRoleDto assginRoleDto);

    List<Long> findRoleIdList(Long userId);
}
