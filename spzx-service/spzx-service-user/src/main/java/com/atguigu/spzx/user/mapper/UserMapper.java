package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserInfo findByUserName(String username);

    void userRegister(UserInfo userInfo);

    void updateLoginInfo(UserInfo userInfo);
}
