package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 购物车Controller控制类
 * Create on 2023/05/25
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        // 日志
        log.info("购物车数据：{}", shoppingCart);

        // 1.设置用户id（确定是哪个用户的购物车）
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        // 2.查询当前菜品或者套餐是否已经存在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            // 菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
            //shoppingCartService.findByDishId(dishId);
        } else {
            // 套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            //shoppingCartService.findBySetMealId(shoppingCart.getSetMealId());
        }

        // SQL:SELECT * FROM shopping_cart WHERE user_id = ? AND dish_id = ?
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(lambdaQueryWrapper);

        if (shoppingCartServiceOne != null) {
            // 3.如果存在，就在原来基础上加1
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        } else {
            // 4.如果不存在，添加购物车
            shoppingCart.setNumber(1);  // 设置默认数量
            shoppingCart.setCreateTime(LocalDateTime.now());    // 设置创建时间，方便其他方法排序使用
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        }

        // 5.返回结果
        return R.success(shoppingCartServiceOne);

    }// 添加购物车方法 代码结束


    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {

        // 1.日志，打印当前行为
        log.info("查看购物车...");

        // 2.构造查询条件和排序条件
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);

        // 3.执行查询
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);

        // 4.返回结果
        return R.success(list);
    }// 查看购物车方法 代码结束


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {

        // SQL:DELETE FROM shopping_cart WHERE user_id = ?
        // 1.构造条件
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        // 2.执行删除
        shoppingCartService.remove(lambdaQueryWrapper);

        // 3.返回结果
        return R.success("清空购物车成功!");
    }// 清空购物车方法 代码结束


    /**
     * 修改购物车菜品/套餐数量
     * @param shoppingCart
     * @return
     * Create on 2023/05/25
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {

        // 日志，测试
        log.info("shoppingCart信息：{}", shoppingCart);

        // 测试获取的num
        /*Long id = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        LambdaQueryWrapper<ShoppingCart> num = queryWrapper.eq(ShoppingCart::getDishId, id);

        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        Integer num2 = shoppingCartServiceOne.getNumber();

        int count = shoppingCartService.count(queryWrapper);

        log.info("querwrapper.eq方法获取的num：{}，service.getone获取的num：{}，service.count获取的num：{}", num,num2,count);
        */

        // 1.获取dishId和setmealId
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        // 2.构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        // 3.判断哪一个不为空，并查询不为空的id对应的菜品/套餐数据
        if (dishId != null) {
            // 查询菜品当前的数量：SELECT number FROM shopping_cart WHERE dish_id = ?; 并赋值给dishNum
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
            int dishNum = shoppingCartServiceOne.getNumber();

            if (dishNum != 0) {
                shoppingCartServiceOne.setNumber(dishNum - 1);  // 进行减1操作
                shoppingCartService.updateById(shoppingCartServiceOne);
            }else {
                shoppingCartService.remove(queryWrapper);
            }

        } else {
            // 查询当前套餐的数量：SELECT number FROM shopping_cart WHERE setmeal_id = ?; 并赋值给setmealNum
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
            ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
            int setmealNum = shoppingCartServiceOne.getNumber();

            if (setmealNum != 0) {
                shoppingCartServiceOne.setNumber(setmealNum - 1);  // 进行减1操作
                shoppingCartService.updateById(shoppingCartServiceOne);
            }else {
                return R.error("套餐数量不足");
            }
        }

        return R.success(shoppingCart);

    }// 修改购物车菜品/套餐数量方法 代码结束
}
