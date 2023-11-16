package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 套餐逻辑实现类
 * Create on 2023/05/16
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

        // 保存套餐的基本信息，操作setmeal表，执行insert操作
        this.save(setmealDto);

        // 通过Dto调用getmealDishes方法，将值赋给setmealDish，并使用lambda函数将其中的item进行getId
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关联信息，操作setm  eal_dish表，执行insert操作
        setmealDishService.saveBatch(setmealDishes);

    }


    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    @Transactional  // 【注意】同时操作两张表，须加入该注释！
    public void removeWithDish(List<Long> ids) {

        // 查询套餐状态，确定可否删除（启售状态不可删除，只有停售状态的才可删除）
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        /* 保证对象为传入的ids列表中，且状态为停售的（0）才可删除；若有状态为1的则不删，sql语句：
        * SELECT COUNT(*) FROM setmeal WHERE id IN ([ids]) AND status = 1; */
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        // 若不能删除，则抛出异常
        int count = this.count(queryWrapper);
        if (count > 0 /*有符合上述两个条件的，则不可删除！*/) {
            throw new CustomException("套餐正在售卖中，不可删除！");
        }

        // 若可删除，现删套餐表(setmeal)中的数据
        this.removeByIds(ids);

        // 再删除关系表中的数据
        // 1.先使用条件构造器构造符合条件的对象
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        // 2.再将该对象传入Service方法中进行删除操作
        setmealDishService.remove(lambdaQueryWrapper);

    }


    /**
     * 根据id更改菜品的status状态【自研】
     * @param ids
     */
    @Override
    public void updateStatus(Integer status, List<Long> ids) {

        // 构造一个条件构造器（根据id查询）
        // !尝试使用lambdaUpdateWrapper!
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId, ids);  // 要使用批量处理的情况，需要将eq改为in

        // 查询到对应id后，将status改为相反的值（0改为1，1改为0）
        updateWrapper.set(Setmeal::getStatus, status);

        // 调用service中update方法，将updateWrapper传入，执行更改语句
        setmealService.update(updateWrapper);
    }
}
