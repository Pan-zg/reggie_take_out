package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;


/**
 * 菜品/套餐分类Service接口
 * Create on 2023/05/15
 */
public interface CategoryService extends IService<Category> {

    void remove(Long ids);
}
