package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;

    @Operation(summary = "分页查询角色列表")
    @PostMapping("/findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody SysRoleDto sysRoleDto){

        PageInfo pageInfo = sysRoleService.findByPage(pageNum,pageSize,sysRoleDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
