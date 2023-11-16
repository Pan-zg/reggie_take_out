package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 套餐-菜品关系实体类
 * Create on 2023/05/18
 */
@Data
public class SetmealDish implements Serializable {

    private static final long SerialVersionUID = 1L;

    // 1.id
    private Long id;


    // 2.套餐id
    private Long setmealId;


    // 3.菜品id
    private Long dishId;


    // 4.菜品名称 （冗余字段）
    private String name;

    // 5.菜品原价
    private BigDecimal price;

    // 6.份数
    private Integer copies;


    // 7.排序
    private Integer sort;

    // 8-11 公共字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    // 12.是否删除
    private Integer isDeleted;

}
