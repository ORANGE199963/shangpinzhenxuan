package com.atguigu.spzx.manager.auth;

import com.atguigu.spzx.model.entity.system.SysUser;

public class ThreadLocalUtil {
    private static ThreadLocal<SysUser> sysUserThreadLocal = new ThreadLocal<SysUser>();

    public static void setSysUser(SysUser sysUser){
        sysUserThreadLocal.set(sysUser);
    }

    public static SysUser getSysUser(){
        return sysUserThreadLocal.get();
    }

    public static void removeSysUser(){
        sysUserThreadLocal.remove();
    }
}
