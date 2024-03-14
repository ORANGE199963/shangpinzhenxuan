package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.base.Region;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserInfo findByUserName(String username);

    void userRegister(UserInfo userInfo);

    void updateLoginInfo(UserInfo userInfo);

    List<Region> findByParentCode(Long parentCode);

    String getPcdName(String districtCode);

    void addAddress(UserAddress userAddress);

    void updateAddressIsDefault(Long userId);

    List<UserAddress> findUserAddressList(Long userId);

    UserAddress findById(Long addressId);

    UserInfo getById(Long userId);
}
