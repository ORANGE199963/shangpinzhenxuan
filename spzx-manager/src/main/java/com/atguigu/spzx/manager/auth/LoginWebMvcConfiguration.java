package com.atguigu.spzx.manager.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginWebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    LoginAuthInterceptor loginAuthInterceptor;
    @Autowired
    UserAuthProperties userAuthProperties;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                .excludePathPatterns(userAuthProperties.getNoAuthUrls())
//                .excludePathPatterns("/admin/system/index/login",
//                        "/admin/system/index/generateValidateCode",
//                        "/doc.html",
//                        "/webjars/**",
//                        "/favicon.ico",
//                        "/v3/api-docs/**")
                .addPathPatterns("/**");
    }
}
