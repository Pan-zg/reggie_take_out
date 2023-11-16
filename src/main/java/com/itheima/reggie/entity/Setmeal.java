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
 * 套餐实体类
 * Create on 2023/05/16
 */
@Data
public class Setmeal implements Serializable{

    private static final long serialVersionUID = 1L;

    // 1.套餐id
    private Long id;


    // 2.分类id
    private Long categoryId;


    // 3.套餐名称
    private String name;


    // 4.套餐价格
    private BigDecimal price;


    // 5.售卖状态 （0:停用 1:启用）
    private Integer status;


    // 6.编码
    private String code;


    // 7.描述信息
    private String description;


    // 8.图片
    private String image;


    // 9-12 公共字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    // 13.是否删除
    private Integer isDeleted;

}
