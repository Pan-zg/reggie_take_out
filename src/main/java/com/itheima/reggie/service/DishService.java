package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;


/**
 * 菜品逻辑Service接口
 * Create on 2023/05/16
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要同时操作两张表：dish、dish_flavor
     * 【注意】在接口中方法前可不加public修饰符，IDEA给出提示：“修饰符 'public' 对于接口成员是冗余的”
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);


    /**
     * 根据id查询菜品和对应口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);


    /**
     * 更新菜品信息和对应的口味信息
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}
