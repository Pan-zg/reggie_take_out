package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * 自定义元数据对象处理器
 * Create on 2023/05/14
 */
@Component
@Slf4j
//@SuppressWarnings("deprecation")
@Deprecated
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        // 日志
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());

        // 使用元对象metaObject设置插入（新建员工信息方法）的公共字段
        metaObject.setValue("createTime", LocalDateTime.now());    // 创建时间
        metaObject.setValue("updateTime", LocalDateTime.now());    // 更新时间
        metaObject.setValue("createUser", BaseContext.getCurrentId()
                /*更改为动态获取登录用户id，下同*/);    // 更新时间
        metaObject.setValue("updateUser", BaseContext.getCurrentId());    // 更新时间
    }


    /**
     * 更新操作，自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        // 日志
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());

        // 插入获取线程id的代码，用于查看当前线程的id
        long id = Thread.currentThread().getId();
        log.info("当前线程id：{}", id);

        // 设置更新（编辑员工信息方法）的公共字段
        metaObject.setValue("updateTime", LocalDateTime.now());    // 更新时间
        metaObject.setValue("updateUser", BaseContext.getCurrentId());    // 更新时间
    }
}
