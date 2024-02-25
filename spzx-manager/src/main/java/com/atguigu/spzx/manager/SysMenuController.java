package com.atguigu.spzx.manager;

import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;


    @Operation(summary = "添加菜单")
    @PostMapping("addMenu")
    public Result addMenu(@RequestBody SysMenu sysMenu){
        sysMenuService.addMenu(sysMenu);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "菜单列表树")
    @GetMapping("menuList")
    public Result menuList(){
        List<SysMenu> list = sysMenuService.menuListByParentId(0L);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }


}
