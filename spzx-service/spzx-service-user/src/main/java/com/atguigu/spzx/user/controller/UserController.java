package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.atguigu.spzx.user.serivce.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户接口管理")
@RestController
@RequestMapping(value="/api/user/")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("getByUserId/{userId}")
    public UserInfo getByUserId(@PathVariable Long userId){
        return userService.getByUserId(userId);
    }
    @GetMapping("findByAddressId/{addressId}")
    public UserAddress findByAddressId(@PathVariable Long addressId){
        return userService.findByAddressId(addressId);
    }

    @PostMapping("userAddress/auth/save")
    public Result addAddress(@RequestBody UserAddress userAddress){
        userService.addAddress(userAddress);
        return Result.build(null,ResultCodeEnum.SUCCESS);

    }

    @GetMapping("/region/findByParentCode/{parentCode}")
    public Result findByParentCode(@PathVariable Long parentCode){
        List<Region> list = userService.findByParentCode(parentCode);

        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/userAddress/auth/findUserAddressList")
    public Result getAddressList(){
        List<UserAddress> list = userService.findUserAddressList();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("userInfo/logout")
    public Result logout(@RequestHeader String token){
        userService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/userInfo/auth/getCurrentUserInfo")
    public Result getUserInfo(@RequestHeader String token){
        System.out.println("当前用户服务--controller：" + Thread.currentThread().getName());
        UserInfoVo userInfoVo = userService.getUserInfo(token);
        return Result.build(userInfoVo,ResultCodeEnum.SUCCESS);
    }
    @PostMapping("userInfo/login")
    public Result login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request){
        String token = userService.login(userLoginDto,request);
        return Result.build(token,ResultCodeEnum.SUCCESS);
    }


    @PostMapping("userInfo/register")
    public Result userRegister(@RequestBody UserRegisterDto userRegisterDto){

        userService.userRegister(userRegisterDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }
}
