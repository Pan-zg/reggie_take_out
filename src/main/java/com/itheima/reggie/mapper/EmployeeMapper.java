package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;


/**
 * Employee的数据存储对象类（相当于DAO）
 * Create on 2023/05/10
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
