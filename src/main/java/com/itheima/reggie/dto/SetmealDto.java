package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;


/**
 * 套餐-菜品DTO类
 * Import on 2023/05/18
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
