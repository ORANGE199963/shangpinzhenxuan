package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> findByParentId(long parentId);

    void addMenu(SysMenu sysMenu);

    void deleteByMenuId(Long menuId);

    void updateMenu(SysMenu sysMenu);

    void deleteByRoleId(AssginMenuDto menuDto);

    void doAssignRoleMenu(@Param("roleId") Long roleId,@Param("menuId") Number menuId,@Param("isHalf") Number isHalf);

    List<Long> getMenuIdList(Long roleId);

    List<SysMenu> findSysMenuList(@Param("userId") Long userId,@Param("parentId") Long parentId);
}
