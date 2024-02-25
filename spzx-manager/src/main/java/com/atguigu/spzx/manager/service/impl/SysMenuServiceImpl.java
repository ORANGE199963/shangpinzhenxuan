package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> menuListByParentId(long parentId) {
        List<SysMenu> menuList = sysMenuMapper.findByParentId(parentId);
        if (!CollectionUtils.isEmpty(menuList)) {
            menuList.forEach(sysMenu -> {
                List<SysMenu> children = this.menuListByParentId(sysMenu.getId());
                sysMenu.setChildren(CollectionUtils.isEmpty(children) ? null : children);
            });
        }
        return menuList;
    }

    @Override
    public void addMenu(SysMenu sysMenu) {
        sysMenuMapper.addMenu(sysMenu);
    }

    @Override
    public void deleteMenu(Long menuId) {
        List<SysMenu> byParentId = sysMenuMapper.findByParentId(menuId);
        if (byParentId != null && byParentId.size() > 0) {
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }

        sysMenuMapper.deleteByMenuId(menuId);
    }

    @Override
    public void updateMenu(SysMenu sysMenu) {
        sysMenuMapper.updateMenu(sysMenu);
    }



    public void deleteMenu2(Long menuId) {
        sysMenuMapper.deleteByMenuId(menuId);
        List<SysMenu> children = sysMenuMapper.findByParentId(menuId);
        if (children != null && children.size() > 0) {
            children.forEach(sysMenu -> {
                this.deleteMenu2(sysMenu.getId());
            });
        }
    }
//    @Override
//    public List<SysMenu> menuListByParentId(long parentId) {
//
//        List<SysMenu> list = sysMenuMapper.findByParentId(parentId);
//
//        list.forEach(sysMenu -> {
//            List<SysMenu> children = sysMenuMapper.findByParentId(sysMenu.getId());
//
//
//            sysMenu.setChildren((children==null || children.size()==0)?null:children);
//        });
//        return list;
//    }

}