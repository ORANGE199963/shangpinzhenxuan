package com.atguigu.spzx.common.exp;

import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

@Data
public class GuiguException extends RuntimeException{
    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;


    public GuiguException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public GuiguException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
        this.resultCodeEnum = resultCodeEnum;
    }
    public GuiguException() {
    }

}
