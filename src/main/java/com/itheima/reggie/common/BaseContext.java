package com.itheima.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户的id
 * Create on 2023/05/15
 */
public class BaseContext {

    // 实例化一个新的Long泛型的ThreadLocal对象
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();


    /**
     * 保存当前登录用户的id
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前登录用户的id
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
