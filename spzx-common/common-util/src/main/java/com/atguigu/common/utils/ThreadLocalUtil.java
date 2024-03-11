package com.atguigu.common.utils;

import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.entity.user.UserInfo;

public class ThreadLocalUtil {


    private static ThreadLocal<SysUser> sysUserThreadLocal = new ThreadLocal<SysUser>();
    private static ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<UserInfo>();


    public static void setUserInfo(UserInfo userInfo){
        userInfoThreadLocal.set(userInfo);
    }
    public static UserInfo getUserInfo(){
        return userInfoThreadLocal.get();
    }
    public static void removeUserInfo(){
        userInfoThreadLocal.remove();
    }


    public static void setSysUser(SysUser sysUser){
        Thread thread;
        sysUserThreadLocal.set(sysUser);
    }
    public static SysUser getSysUser(){
        return sysUserThreadLocal.get();
    }
    public static void removeSysUser(){
        sysUserThreadLocal.remove();//将当前Thread对象的map中，根据key去清除数据
    }

}
