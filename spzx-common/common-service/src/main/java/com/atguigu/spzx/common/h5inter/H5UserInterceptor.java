package com.atguigu.spzx.common.h5inter;

import com.alibaba.fastjson2.JSON;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.model.entity.user.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
public class H5UserInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("当前拦截器：" + Thread.currentThread().getName());

        String token = request.getHeader("token");
        String key = "user:login:"+token;
        String jsonString = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(jsonString)){
            UserInfo userInfo = JSON.parseObject(jsonString, UserInfo.class);
            ThreadLocalUtil.setUserInfo(userInfo);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("请求处理完成");
        ThreadLocalUtil.removeUserInfo();
    }
}
