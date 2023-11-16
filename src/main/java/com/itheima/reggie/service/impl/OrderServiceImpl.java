package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * 订单Service实现类
 * Create on 2023/05/25
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;


    /**
     * 用户下单Impl方法
     * @param orders
     * Create on 2023/05/26
     */
    @Override
    public void submit(Orders orders) {

        // 1.获得当前用户id
        Long userId = BaseContext.getCurrentId();

        // 2.查询当前用户购物车信息
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCart = shoppingCartService.list(queryWrapper);
        // 若购物车为空则抛出异常（不可下单）
        if (shoppingCart == null || shoppingCart.size() == 0) {
            throw new RuntimeException("购物车为空，无法下单！");
        }

        // 2.1查询用户数据
        User user = userService.getById(userId);
        // 2.2查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        // 若地址为空则抛出异常（不可下单）
        if (addressBook == null) {
            throw new RuntimeException("地址不存在，无法下单！");
        }

        // 3.向订单表插入数据（一个订单对应一条数据）
        long orderId = IdWorker.getId();    // 订单号
        // 3.1计算总金额
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCart.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue()); // 总金额
            return orderDetail;
        }).collect(Collectors.toList());
        // 3.2设置订单数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get())); // 总金额，需要遍历购物车数据
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        // 3.3保存订单数据
        this.save(orders);

        // 4.向订单详情表插入数据（可能包含多条数据）
        orderDetailService.saveBatch(orderDetails);

        // 5.下单完成后清空购物车
        shoppingCartService.remove(queryWrapper);

    }// 用户下单方法 代码结束
}
