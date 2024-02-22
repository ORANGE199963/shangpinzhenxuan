package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录登出模块")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ValidateCodeService validateCodeService;


    @Operation(summary = "退出接口")
    @DeleteMapping("logout")
    public Result logout(@RequestHeader String token){
        sysUserService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "根据请求头token查询用户信息")
    @GetMapping("getUserInfo")
    public Result getUserInfo(@RequestHeader String token){
        SysUserVo sysUserVo = sysUserService.getUserInfo(token);
        return Result.build(sysUserVo,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "登录接口")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "获取图形验证码")
    @GetMapping("generateValidateCode")
    public Result generateValidateCode(){
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }
}
