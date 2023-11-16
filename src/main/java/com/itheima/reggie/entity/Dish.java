package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 菜品实体类
 * Create on 2023/05/16
 */
@Data
public class Dish implements Serializable{

    public static final long SerialVersionUID = 1L;

    // 1.菜品id
    private Long id;


    // 2.菜品名称
    private String name;


    // 3.菜品分类id
    private Long categoryId;


    // 4.菜品价格
    private BigDecimal price;


    // 5.商品码
    private String code;


    // 6.图片
    private String image;


    // 7.描述信息
    private String description;


    // 8.售卖状态（0 停售；1 起售）
    private Integer status;


    // 9，顺序
    private Integer sort;


    // 10-13 公共字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    // 14.是否删除（大概率在表中也没有匹配的，需要注释掉）
    private Integer isDeleted;

}
