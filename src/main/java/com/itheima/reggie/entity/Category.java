package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 菜品/套餐分类实体类
 * Create on 2023/05/15
 */
@Data
public class Category implements Serializable {

    public static final long serialVersionUID = 1L;     // 这一行代码不影响id的生成！

    private Long id;    // 注意，此处的数据类型为Long而不是long，否则将无法添加id！


    // 类型 1为菜品分类 2为套餐分类
    private Integer type;


    // 分类名称
    private String name;


    // 顺序
    private Integer sort;


    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    // 创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    // 更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    // 是否删除
    //private Integer isDeleted;

}
