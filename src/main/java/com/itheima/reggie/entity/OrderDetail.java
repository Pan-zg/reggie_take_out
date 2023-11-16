package com.itheima.reggie.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 订单详情实体类
 * Create on 2023/05/25
 */
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    // 1.id
    private Long id;


    // 2.名称
    private String name;


    // 3.订单id
    private Long orderId;


    // 4.菜品id
    private Long dishId;


    // 5.套餐id
    private Long setmealId;


    // 6.口味
    private String dishFlavor;


    // 7.数量
    private Integer number;


    // 8.金额
    private BigDecimal amount;


    // 9.图片
    private String image;

}
