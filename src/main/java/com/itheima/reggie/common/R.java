package com.itheima.reggie.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;


/**
 * 通用返回结果类（将相应的结果包装后返回给前端）
 * @param <T>
 * Create on 2023/05/10
 */
@Data
public class R<T> {

    private Integer code;   // 编码：1表示成功；0和其他数字表示失败

    private String msg;    // 错误信息

    private T data;    // 数据

    private Map map = new HashMap();    // 动态数据


    // 成功返回的结果
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    // 出现错误返回的结果
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    // 添加结果（？）
    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
