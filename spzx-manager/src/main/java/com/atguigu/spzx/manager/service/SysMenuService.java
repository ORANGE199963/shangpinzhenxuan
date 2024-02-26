package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.system.SysMenuVo;

import java.util.List;
import java.util.Map;

public interface SysMenuService {

    List<SysMenu> menuListByParentId(long parentId);

    void addMenu(SysMenu sysMenu);

    void deleteMenu(Long menuId);

    void updateMenu(SysMenu sysMenu);

    Map getMenuListAndMenuIdList(Long roleId);

    void doAssignMenu(AssginMenuDto menuDto);

    List<SysMenuVo> getMenuListByUserId(Long userId, Long parentId);
}
