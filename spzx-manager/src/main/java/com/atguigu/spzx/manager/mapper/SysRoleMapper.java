package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    List<SysRole> selectList(SysRoleDto sysRoleDto);

    void deleteById(Long roleId);

    void addRole(SysRole sysRole);

    void updateRole(SysRole sysRole);

    List<SysRole> findRoleList();

    void deleteFromUserRoleByUserId(Long userId);

    void doAssign(@Param("roleId") Long roleId,@Param("userId") Long userId);

    List<Long> findRoleIdList(Long userId);
}
