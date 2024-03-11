package com.atguigu.spzx.manager.auth;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String requestMethod = request.getMethod();
        if(requestMethod.equals("OPTIONS")){
            return true;
        }

        String token = request.getHeader("token");
        String key = "user:login:" + token;
        String jsonString = redisTemplate.opsForValue().get(key);


        if(StringUtils.isEmpty(jsonString)){
           return this.response208(response);

        }



        redisTemplate.expire(key,30, TimeUnit.MINUTES);

//        new ThreadLocal<SysUser>().set(JSON.parseObject(jsonString,SysUser.class));

        ThreadLocalUtil.setSysUser(JSON.parseObject(jsonString, SysUser.class));
        return true;
    }

    private boolean response208(HttpServletResponse response) {
        Result noLogin = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        String jsonResult208 = JSON.toJSONString(noLogin);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.write(jsonResult208);

        if(writer!=null){
            writer.close();
        }

        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.removeSysUser();
    }
}
