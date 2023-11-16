package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 菜品口味实体类
 * Create on 2023/05/17
 */
@Data
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    // 1.id
    private Long id;


    // 2.菜品id
    private long dishId;

    // 3.口味名称
    private String name;


    // 4.口味数据list
    private String value;


    // 5-8 公共数据字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    // 9.是否删除
    //private Integer isDelete;

}
