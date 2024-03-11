package com.atguigu.spzx.manager.service.impl;

import aspect.SysOperLogService;
import com.atguigu.spzx.manager.mapper.SysOperLogMapper;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogServiceImpl  implements SysOperLogService {

    @Autowired
    SysOperLogMapper sysOperLogMapper;


    //会开启一个新的子线程，对该方法的调用
    @Async
    @Override
    public void addSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.addOperLog(sysOperLog);
    }
}
