package com.atguigu.spzx.manager;

import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.model.entity.system.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
public class ManagerTest {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Test
    public void test1(){
        List<SysUser> list = sysUserMapper.findAll();
        list.forEach(System.out::println);
    }

    @Test
    public void test2(){
        redisTemplate.opsForValue().set("project","尚品甄选");
    }
}
