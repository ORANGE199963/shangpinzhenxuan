package com.atguigu.spzx.common.log.annotation;

import com.atguigu.spzx.common.log.enums.OperatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD}) //注解作用范围
@Retention(RetentionPolicy.RUNTIME) //注解的生命周期(需要在运行时去动态获取注解信息)
public @interface Log {		

    public String title() ;												// 模块标题
    public OperatorType operatorType() default OperatorType.MANAGE;		// 操作人类别（后台用户，手机端用户，其它）
    public int businessType() ;     									// 业务类型（0其它 1新增 2修改 3删除）
    public boolean isSaveRequestData() default true;   					// 是否保存请求参数
    public boolean isSaveResponseData() default true;  					// 是否保存响应结果
    
}