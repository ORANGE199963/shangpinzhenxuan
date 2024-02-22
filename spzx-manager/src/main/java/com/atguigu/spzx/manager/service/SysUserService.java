package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.manager.controller.SysUserVo;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.vo.system.LoginVo;

public interface SysUserService {
    LoginVo login(LoginDto loginDto);

    SysUserVo getUserInfo(String token);

    void logout(String token);
}
