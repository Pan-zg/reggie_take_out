package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;


/**
 * AddressBookService实现类
 * Create on 2023/05/23
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
