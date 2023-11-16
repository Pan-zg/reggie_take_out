package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 订单Controller
 * Create on 2023/05/25
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {

        // 日志，测试
        log.info("订单数据：{}", orders);

        // 调用service层的submit方法
        orderService.submit(orders);

        // 返回结果
        return R.success("下单成功！");

    }// 用户下单方法 代码结束


    /**
     * 订单信息分页查询方法
     * @param page
     * @param pageSize
     * @param number
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number/*订单号*//*注：先暂时不添加起始时间字段*/) {

        // 1.构造分页对象
        Page pageInfo = new Page<>(page, pageSize);

        // 2.构造查询条件对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        // 3.添加过滤条件（number不为空才传入）
        queryWrapper.like(StringUtils.isNotBlank(number), Orders::getNumber, number);

        // 4.添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime); // 按照下单时间排序

        // 5.执行查询
        orderService.page(pageInfo, queryWrapper);

        // 6.返回结果
        return R.success(pageInfo);

    }// 订单信息分页查询方法 代码结束
}
