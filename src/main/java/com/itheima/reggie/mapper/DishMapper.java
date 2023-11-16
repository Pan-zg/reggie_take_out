package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;


/**
 * 菜品的Mapper接口
 * Create on 2023/05/16
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
