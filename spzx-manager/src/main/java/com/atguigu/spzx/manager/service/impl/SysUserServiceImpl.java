package com.atguigu.spzx.manager.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Override
    public LoginVo login(LoginDto loginDto) {

        String userName = loginDto.getUserName();
        String password = loginDto.getPassword();
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
//            throw new RuntimeException("用户名或密码为空");
            throw new GuiguException(ResultCodeEnum.USERNAME_OR_PASSWORD_IS_EMPTY);
        }

        SysUser sysUser = sysUserMapper.findByUsername(userName);
        if (sysUser==null){
//            throw new RuntimeException("该用户不存在");
            throw new GuiguException(ResultCodeEnum.USER_NOT_EXISTS);
        }
        String passwordFromDb = sysUser.getPassword();
        if(!passwordFromDb.equals(DigestUtil.md5Hex(password))){
//            throw new RuntimeException("密码不正确");
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        if(sysUser.getStatus()==0){
//            throw new RuntimeException("该用户被禁用");
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }

        String token = UUID.randomUUID().toString().replaceAll("-","");
        String key = "user:login:" + token;
        String value = JSON.toJSONString(sysUser);

        redisTemplate.opsForValue().set(key,value,30, TimeUnit.MINUTES);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }
}
