package com.itheima.reggie.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


/**
 * 用户实体类
 * Create on 2023/05/23
 */
@Data
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    // 1.id
    private Long id;

    // 2.姓名
    private String name;

    // 3.手机号
    private String phone;

    // 4.性别（女：0；男：1）
    private String sex;

    // 5.身份证号
    private String idNumber;

    // 6.头像
    private String avatar;

    // 7.状态（0：禁用，1：正常）
    private Integer status;

}
