package com.atguigu.spzx.user.serivce;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    void userRegister(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto, HttpServletRequest request);

    UserInfoVo getUserInfo(String token);

    void logout(String token);
}
