package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


/**
 * 文件上传下载控制类
 * Create on 2023/05/16
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        // 日志，测试用
        log.info(file.toString() /*测试后发现，file是一个临时文件，需要转存到指定位置，
                                否则本次请求完成后临时文件会删除*/);

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取“.jpg”字符串，用于后续拼接，并按照建议添加了非空判断（if != null...）
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 使用UUID重新生成文件名，防止文件名重复造成文件覆盖；同时，动态获取图片后缀“.jpg”，与生成的随机文件名拼接
        String fileName = UUID.randomUUID().toString() + suffix;

        // 创建一个目录对象，并判断其是否存在，不存在则创建之
        File dir = new File(basePath);
        if (!dir.exists()) {
            // 创建目录
            dir.mkdirs();
        }

        try {
            // 将临时文件转存到指定目录
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 给页面返回文件名
        return R.success(fileName);

    }// 文件上传方法代码结束


    /**
     * 文件下载方法
     * Create on 2023/05/17
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        // 使用try...catch...进行异常处理
        try {
            // 输入流，用于读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 输出流，将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            // 设置图片格式
            response.setContentType("image/jpge");
            // 将输入流读取的文件内容写入到输出流中
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1 /*将读入的bytes赋给len，
                                                            且只要不等于-1.说明还没读完*/) {
                outputStream.write(bytes, 0, len /*从0开始一直写到len索引的长度为止*/);
                outputStream.flush();
            }

            // 别忘了关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }// 文件下载方法代码结束
}
