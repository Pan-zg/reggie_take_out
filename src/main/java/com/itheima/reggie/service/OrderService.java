package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;


/**
 * 订单Service接口
 * Create on 2023/05/25
 */
public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
