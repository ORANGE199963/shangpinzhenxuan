package com.atguigu.spzx.user.serivce;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    void userRegister(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto, HttpServletRequest request);

    UserInfoVo getUserInfo(String token);

    void logout(String token);

    List<Region> findByParentCode(Long parentCode);

    void addAddress(UserAddress userAddress);

    List<UserAddress> findUserAddressList();

    UserAddress findByAddressId(Long addressId);

    UserInfo getByUserId(Long userId);
}
