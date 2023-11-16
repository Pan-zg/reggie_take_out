package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 菜品/套餐分类的Service实现类
 * Create on 2023/05/15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {

        // 菜品关联查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // 查询当前分类是否已关联菜品，若关联则抛出业务异常
        if (count1 > 0) {
            // 已关联菜品，则抛出异常
            throw new CustomException("当前分类已关联菜品，不可删除！");
        }

        // 套餐关联查询
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        // 查询当前分类是否已关联套餐，若关联则抛出业务异常
        if (count2 > 0) {
            // 已关联套餐，抛出异常
            throw new CustomException("当前分类已关联套餐，不可删除！");
        }

        // 若均无关联则正常删除
        super.removeById(ids);

    }
}
