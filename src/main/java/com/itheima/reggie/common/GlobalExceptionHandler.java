package com.itheima.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * Create on 2023/05/11
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class} /*处理器会拦截有这两个注解的类*/)
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {  // 用户名重复的情况
            String[] split = ex.getMessage().split(" ");    // 获取并分隔报错信息中的用户名字段
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("存在未知错误，注册失败");

        //return null;
    }// 异常处理方法 代码结束



    /**
     * 自定义业务异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage()); // 记录日志

        return R.error(ex.getMessage());    // 返回错误信息

        //return null;
    }// 自定义异常处理方法 代码结束
}
