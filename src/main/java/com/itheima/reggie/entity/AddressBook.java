package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 地址簿实体类
 * Create on 2023/05/22
 */
@Data
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    // 1.id
    private Long id;

    // 2.用户id
    private Long userId;


    // 3.收货人
    private String consignee;


    // 4.手机号
    private String phone;


    // 5.性别 (女：0，男：1)
    private String sex;


    // 6.省级区划编号
    private String provinceCode;


    // 7.省级名称
    private String provinceName;


    // 8.市级区划编号
    private String cityCode;


    // 9.市级名称
    private String cityName;


    // 10，区级区划编号
    private String districtCode;


    // 11.区级名称
    private String districtName;


    // 12.详细地址
    private String detail;


    // 13.标签
    private String label;

    // 14.是否默认地址（0：否，1：是）
    private Integer isDefault;

    // 15 - 18 公共字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    // 19.是否删除
    //private Integer isDeleted;

}
