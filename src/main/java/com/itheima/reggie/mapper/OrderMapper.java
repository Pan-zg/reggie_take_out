package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;


/**
 * 订单Mapper接口
 * Create on 2023/05/25
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
