package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, SysRoleDto sysRoleDto) {

        PageHelper.startPage(pageNum,pageSize);
        List<SysRole> list = sysRoleMapper.selectList(sysRoleDto);

        return new PageInfo(list);
    }

    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.deleteById(roleId);
    }

    @Override
    public void addRole(SysRole sysRole) {
        sysRoleMapper.addRole(sysRole);
    }

    @Override
    public void updateRole(SysRole sysRole) {
        sysRoleMapper.updateRole(sysRole);
    }

    @Override
    public List<SysRole> findRoleList() {
        sysRoleMapper.findRoleList();
        return sysRoleMapper.findRoleList();
    }

    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {
        sysRoleMapper.deleteFromUserRoleByUserId(assginRoleDto.getUserId());

        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        for (Long roleId : roleIdList) {
            sysRoleMapper.doAssign(roleId,assginRoleDto.getUserId());
        }
    }

    @Override
    public List<Long> findRoleIdList(Long userId) {
        return sysRoleMapper.findRoleIdList(userId);
    }
}
