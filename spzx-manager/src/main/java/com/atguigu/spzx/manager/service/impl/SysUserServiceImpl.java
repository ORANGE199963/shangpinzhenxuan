package com.atguigu.spzx.manager.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.manager.controller.SysUserVo;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Override
    public LoginVo login(LoginDto loginDto) {

        this.checkCaptchaCode(loginDto);

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

    @Override
    public SysUserVo getUserInfo(String token) {
//        String key = "user:login:" + token;
//        String jsonString = redisTemplate.opsForValue().get(key);
//
//        if(StringUtils.isEmpty(jsonString)){
//            throw new GuiguException(ResultCodeEnum.TOKEN_INVALIDATE);
//        }

//        SysUser sysUser = JSON.parseObject(jsonString, SysUser.class);

        SysUser sysUser = ThreadLocalUtil.getSysUser();


        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setId(sysUser.getId());
        sysUserVo.setName(sysUser.getName());
        sysUserVo.setAvatar(sysUser.getAvatar());
        return sysUserVo;
    }

    @Override
    public void logout(String token) {
        String key = "user:login:" + token;
        redisTemplate.delete(key);
    }

    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> list = sysUserMapper.findByPage(sysUserDto);
        return new PageInfo(list);
    }

    @Override
    public void addUser(SysUser sysUser) {
        String userName = sysUser.getUserName();
        SysUser sysUserFromDb = sysUserMapper.findByUsername(userName);
        if(sysUserFromDb!=null){
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        String password = sysUser.getPassword();
        String passwordByMd5 = DigestUtil.md5Hex(password);
        sysUser.setPassword(passwordByMd5);

        sysUserMapper.addUser(sysUser);
    }

    @Override
    public void editUser(SysUser sysUser) {
        Long id = sysUser.getId();
        SysUser sysUserById = sysUserMapper.findById(id);

        if(!sysUserById.getUserName().equals(sysUser.getUserName())){
            SysUser byUsername = sysUserMapper.findByUsername(sysUser.getUserName());
            if(byUsername!=null){
                throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
            }
        }
        sysUserMapper.editUser(sysUser);
    }

    @Override
    public void deleteById(Long id) {
        sysUserMapper.deleteById(id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        for(Long id : ids) {
            sysUserMapper.deleteById(id);
        }
    }


    private void checkCaptchaCode(LoginDto loginDto) {
        String codeKey = loginDto.getCodeKey();
        String key = "user:code:" + codeKey;
        String codeFromRedis = redisTemplate.opsForValue().get(key);

        if(StringUtils.isEmpty(codeFromRedis)){
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_EXPIRED);
        }

        String captcha = loginDto.getCaptcha();
        if(!codeFromRedis.equals(captcha)){
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        redisTemplate.delete(key);
    }
}
