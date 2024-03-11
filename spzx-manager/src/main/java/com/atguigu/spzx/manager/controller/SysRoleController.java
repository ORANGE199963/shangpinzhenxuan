package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.common.log.annotation.Log;
import com.atguigu.spzx.common.log.enums.BusinessType;
import com.atguigu.spzx.common.log.enums.OperatorType;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;


    @Operation(summary = "为用户分配角色")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssginRoleDto assginRoleDto){
        sysRoleService.doAssign(assginRoleDto);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "查询所有角色列表和当前用户已分配的角色id集合")
    @GetMapping("findRoleListAndRoleIdList/{userId}")
    public Result findRoleList(@PathVariable Long userId){
        List<SysRole> roleList = sysRoleService.findRoleList();
        List<Long> roleIdList = sysRoleService.findRoleIdList(userId);

        Map map = new HashMap();
        map.put("roleList",roleList);
        map.put("roleIdList",roleIdList);

        return Result.build(map,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "修改角色")
    @PutMapping("updateRole")
    public Result updateRole(@RequestBody SysRole sysRole){
        sysRoleService.updateRole(sysRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Log(title = "角色管理:添加角色",operatorType = OperatorType.MANAGE,businessType = BusinessType.ADD,isSaveRequestData = true,isSaveResponseData = true)
    @Operation(summary = "添加角色")
    @PostMapping("addRole")
    public Result addRole(@RequestBody SysRole sysRole){
        sysRoleService.addRole(sysRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "根据角色id逻辑删除")
    @DeleteMapping("deleteById/{roleId}")
    public Result deleteById(@PathVariable Long roleId){
        sysRoleService.deleteById(roleId);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "分页查询角色列表")
    @PostMapping("/findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody SysRoleDto sysRoleDto){

        PageInfo pageInfo = sysRoleService.findByPage(pageNum,pageSize,sysRoleDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
