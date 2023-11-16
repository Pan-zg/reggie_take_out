package com.itheima.reggie.common;

/**
 * 自定义业务异常类
 * 2023/05/16
 */
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message); // 调用父类的构造方法
    }
}
