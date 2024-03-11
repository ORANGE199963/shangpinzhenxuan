package com.atguigu.spzx.user.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.atguigu.spzx.user.mapper.UserMapper;
import com.atguigu.spzx.user.serivce.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    UserMapper userMapper;

    @Override
    public void userRegister(UserRegisterDto userRegisterDto) {
        String username = userRegisterDto.getUsername();//用户输入的手机号
        String code = userRegisterDto.getCode();//用户输入的短信验证码
        String password = userRegisterDto.getPassword();//密码
        String nickName = userRegisterDto.getNickName();
        if(StringUtils.isEmpty(username)){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        if(StringUtils.isEmpty(code)){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        if(StringUtils.isEmpty(password)){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        if(StringUtils.isEmpty(nickName)){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        //2、校验短信验证码是否正确。从redis中根据手机号获取验证码，和用户输入的验证码比较（VALIDATECODE_ERROR）
        String key = "user:code:" + username; //注意：把你的redis打开，你看你的key怎么写的
        String codeFromRedis = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(codeFromRedis)){
            throw new GuiguException(209,"验证码已过期");
        }

        if (!codeFromRedis.equals(code)){
            throw new GuiguException(209,"验证码不正确");
        }


        //3、根据用户名userName（也就是手机号）查询用户是否存在（username不允许重复，USER_NAME_IS_EXISTS）
        UserInfo userInfo = userMapper.findByUserName(username);
        if(userInfo!=null){
            throw new GuiguException(209,"该手机号已被占用");
        }


        //4、保存用户信息（username和phone相同；密码需要加密；状态默认正常）
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPhone(username);//username 和 phone 字段都存储手机号
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        userInfo.setStatus(1);//用户状态默认=1
        userInfo.setAvatar("http://139.198.127.41:9000/spzx/20230525/665832167-5_u_1 (1).jpg");

        userMapper.userRegister(userInfo);


        //5、删除Redis中该手机号的验证码
        stringRedisTemplate.delete(key);
    }

    @Override
    public String login(UserLoginDto userLoginDto, HttpServletRequest request) {
        String username = userLoginDto.getUsername();//手机号
        String password = userLoginDto.getPassword();//密码
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }


        //根据手机号查询该用户是否存在
        UserInfo userInfo = userMapper.findByUserName(username);
        if(userInfo==null){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);//用户名或者密码错误
        }

        //密码校验
        if (!userInfo.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);//用户名或者密码错误
        }

        //状态校验
        if(userInfo.getStatus().intValue()==0){
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }

        userInfo.setLastLoginIp(request.getRemoteHost());
        userInfo.setLastLoginTime(new Date());
        //UPDATE `user_info` SET last_login_ip = #{} , last_login_time = #{} where id = #{}
        userMapper.updateLoginInfo(userInfo);

        String token = UUID.randomUUID().toString().replaceAll("-","");
        String key = "user:login:" + token;
        String jsonString = JSON.toJSONString(userInfo);
        stringRedisTemplate.opsForValue().set(key,jsonString,30, TimeUnit.MINUTES);

        return token;//给前端返回的token，将来前端发起请求时，会将token放在请求头中，传给后端接口
    }

    @Override
    public UserInfoVo getUserInfo(String token) {
        System.out.println("当前用户服务--service：" + Thread.currentThread().getName());
//        String key = "user:login:" + token;
//        String jsonString = stringRedisTemplate.opsForValue().get(key);
//        if(StringUtils.isEmpty(jsonString)){
//            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);//208，用户未登录
//        }
//
//        UserInfo userInfo = JSON.parseObject(jsonString, UserInfo.class);

        UserInfo userInfo = ThreadLocalUtil.getUserInfo();

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setNickName(userInfo.getNickName());
        userInfoVo.setAvatar(userInfo.getAvatar());

        return userInfoVo;
    }

    @Override
    public void logout(String token) {
        String key = "user:login:" + token;
        stringRedisTemplate.delete(key);
    }


}
