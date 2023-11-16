package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;


/**
 * 购物车Mapper接口
 * Create on 2023/05/25
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
