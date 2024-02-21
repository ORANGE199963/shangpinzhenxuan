package com.atguigu.spzx.common.exphandler;

import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public Result handlerException(Exception exception){
        exception.printStackTrace();
        return Result.build(null,299,exception.getMessage());
    }

    @ExceptionHandler(value = GuiguException.class)
    public Result handlerException(GuiguException exception){
        exception.printStackTrace();

        return Result.build(null,exception.getCode(),exception.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Result handlerException(NullPointerException exception){
        exception.printStackTrace();

        return Result.build(null,299,exception.getMessage());
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public Result handlerException(ArithmeticException exception){
        exception.printStackTrace();

        return Result.build(null,299,exception.getMessage());
    }
}
