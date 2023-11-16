package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;


/**
 * 菜品/套餐分类的Mapper接口
 * Create on 2023/05/15
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
