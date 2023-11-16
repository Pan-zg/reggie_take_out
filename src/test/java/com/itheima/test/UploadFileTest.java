package com.itheima.test;

import org.junit.jupiter.api.Test;

public class UploadFileTest {


    /**
     * 用于测试截取上传文件的后缀的代码
     * Create on 2023/05/17
     */
    @Test
    public void test1() {

        String fileName = "2a8d2asd21hah0959033.jpg";
        //String suffix = null;
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        System.out.printf("后缀为：%s", suffix);    // 尝试使用格式化输出方法printf输出
    }
}
