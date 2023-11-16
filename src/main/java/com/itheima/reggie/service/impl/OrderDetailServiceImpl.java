package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;


/**
 * 订单详情Service实现类
 * Create on 2023/05/25
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
