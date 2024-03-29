package com.atguigu.spzx.model.vo.common;

import lombok.Getter;

@Getter // 提供获取属性值的getter方法
public enum ResultCodeEnum {

    SUCCESS(200 , "操作成功") ,
    LOGIN_ERROR(201 , "用户名或者密码错误"),
    VALIDATECODE_ERROR(202 , "验证码错误") ,
    LOGIN_AUTH(208 , "用户未登录"),
    USER_NAME_IS_EXISTS(209 , "用户名已经存在"),
    SYSTEM_ERROR(9999 , "您的网络有问题请稍后重试"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    ACCOUNT_STOP( 216, "账号已停用"),

    USERNAME_OR_PASSWORD_IS_EMPTY(301 , "用户名或者密码为空"),
    USER_NOT_EXISTS(302 , "用户不存在"),
    VALIDATECODE_EXPIRED(303 , "验证码已过期") ,
    TOKEN_INVALIDATE(304 , "token令牌无效") ,
    FILE_TYPE_ERROR(305 , "文件类型不正确") ,
    CATEGORY_BRAND_EXISTS(306 , "该分类和品牌关系已建立") ,

    STOCK_LESS( 219, "库存不足"),

    ;

    private Integer code ;      // 业务状态码
    private String message ;    // 响应消息

    private ResultCodeEnum(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }

}
