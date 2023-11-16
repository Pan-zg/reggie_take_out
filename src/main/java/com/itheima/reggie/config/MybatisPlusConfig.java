package com.itheima.reggie.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置MybatisPlus中的分页插件
 * Create on 2023/05/11
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 实例化一个MP拦截器对象，并在其中再实例化一个PI拦截器，最后返回之
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();   // 实例化MP拦截器对象
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());   /*实例化PI拦截器对象，
                                                                                        并添加到MP拦截器中*/
        return mybatisPlusInterceptor;

        //return null;
    }
}
