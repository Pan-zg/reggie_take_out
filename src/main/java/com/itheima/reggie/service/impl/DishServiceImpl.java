package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜品逻辑实现类
 * Create on 2023/05/16
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        // 保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();    // 菜品id

        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }


    /**
     * 根据id查询菜品和对应口味信息 的实现方法
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        // 从dish表查询当前菜品信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 从dish_flavor表查询当前菜品对应的口味信息
        // 创建一个构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }


    /**
     * 更新菜品信息和对应的口味信息 的实现方法
     * @param dishDto
     */
    @Override
    @Transactional  // 注意这里必须加入该注释，否则可能报错
    public void updateWithFlavor(DishDto dishDto) {

        // 更新dish表菜品信息
        this.updateById(dishDto);

        // 清除当前菜品对应在dish_flavor表中的口味信息 ———— delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        // 添加当前提交的菜品对应的口味信息 ———— insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }
}
