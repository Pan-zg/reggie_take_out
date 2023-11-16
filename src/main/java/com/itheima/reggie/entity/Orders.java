package com.itheima.reggie.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 订单实体类
 * Create on 2023/05/25
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    // 1.id
    private Long id;


    // 2.订单号
    private String number;


    // 3.订单状态(1待付款，2待派送，3已派送，4已完成，5已取消)
    private Integer status;


    // 5.下单用户id
    private Long userId;


    // 6.地址id
    private Long addressBookId;


    // 7.下单时间
    private LocalDateTime orderTime;


    // 8.结账时间
    private LocalDateTime checkoutTime;


    // 9.支付方式(1微信，2支付宝)
    private Integer payMethod;


    // 10.实收金额
    private BigDecimal amount;


    // 11.备注
    private String remark;


    // 12.用户名
    private String userName;


    // 13.手机号
    private String phone;


    // 14.地址
    private String address;


    // 15.收货人
    private String consignee;

}
