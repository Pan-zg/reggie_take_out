package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户信息mapper接口
 * Create on 2023/05/23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
