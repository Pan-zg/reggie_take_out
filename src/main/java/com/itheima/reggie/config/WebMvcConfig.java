package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


/**
 * WebMvc配置类
 * Create on 2023/05/08
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //super.addResourceHandlers(registry);

        // 将静态资源文件和静态资源目录重新定为 ./backend 和 ./front 目录；并添加一个日志 log 用于记录。
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }


    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 日志
        log.info("扩展消息转换器...");
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jacksonj将Java对象转为Json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到mvc框架的转换器容器集合中
        converters.add(0 /*索引置为0，使得converters能优先调用我们的messageConverter*/, messageConverter);

        //super.extendMessageConverters(converters);
    }
}
