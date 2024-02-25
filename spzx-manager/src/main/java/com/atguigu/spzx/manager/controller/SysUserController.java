package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;


    @Operation(summary = "删除用户")
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        sysUserService.deleteById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "修改用户")
    @PostMapping("editUser")
    public Result editUser(@RequestBody SysUser sysUser){

        sysUserService.editUser(sysUser);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }


    @Operation(summary = "添加用户")
    @PostMapping("addUser")
    public Result addUser(@RequestBody SysUser sysUser){

        sysUserService.addUser(sysUser);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "查询用户列表")
    @PostMapping("findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize, @RequestBody SysUserDto sysUserDto){
        PageInfo pageInfo = sysUserService.findByPage(pageNum,pageSize,sysUserDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
