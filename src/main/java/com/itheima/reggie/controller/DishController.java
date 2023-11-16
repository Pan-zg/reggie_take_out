package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜品管理控制类
 * Create on 2023/05/16
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品功能
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        // 日志，测试用
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功！");
    }// 新增菜品的方法 代码结束


    /**
     * 菜品信息分页查询方法
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        // 构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();   // 增加Dto，实现分类信息输出

        // 构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like/*使用like模糊查询*/(name != null, Dish::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 执行分页查询并返回结果
        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records"/*这个属性可忽略，
                                                                                后续自行处理获取*/);
        // 获取records包含的信息（records包含Dish和DishDto两部分）
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {   /*这种 "(parameter) -> {expression}"
                                                                的用法称为Lambda表达式*/
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();     // 分类id
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) { // 加入非空判断，防止category为空的报错
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        // 将上面获取的list放入dishDto中
        dishDtoPage.setRecords(list);
        // 返回结果
        return R.success(dishDtoPage);

    }// 菜品分页查询功能方法代码结束


    /**
     * 根据id查询菜品和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        // 调用dishService的getByIdWithFlavor方法同时查询两张表并返回结果
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);

    }// 根据id查询对应菜品和对应的口味信息的方法 代码结束


    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {

        // 调用自定义的updateWithFlavor方法
        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功！");
    }// 修改菜品信息方法 代码结束


    /**
     * 根据条件查询对应的菜品数据（更新版，增加口味信息）
     * @param dish
     * @return
     */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {

        // 构造条件查询对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        // 添加售卖状态条件(只查询起售状态的菜品)
        queryWrapper.eq(Dish::getStatus, 1);

        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        // 调用dishSerivce
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }// 根据条件查询对应的菜品数据方法 代码结束*/

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        // 构造条件查询对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        // 添加售卖状态条件(只查询起售状态的菜品)
        queryWrapper.eq(Dish::getStatus, 1);

        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        // 调用dishSerivce
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();     // 分类id
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) { // 加入非空判断，防止category为空的报错
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            Long dishId = item.getId(); // 当前菜品id

            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>(); // 构造条件查询对象
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            // SQL语句:select * from dish_flavor where dish_id = ?;
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);   // 查询当前菜品对应的口味信息
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }// 根据条件查询对应的菜品数据方法 代码结束
}
