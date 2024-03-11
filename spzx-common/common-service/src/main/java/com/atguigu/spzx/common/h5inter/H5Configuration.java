package com.atguigu.spzx.common.h5inter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class H5Configuration implements WebMvcConfigurer {

    @Autowired
    H5UserInterceptor h5UserInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(h5UserInterceptor)
                .addPathPatterns("/**");
    }
}
