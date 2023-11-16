package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


/**
 * 菜品数据传输对象DTO
 * Import on 2023/05/17
 */
@Data   /*在使用@Data注释后，有如下警示信息：
    “生成 equals/hashCode 实现，但即使此类未扩展 java.lang.Object，也不调用超类。
    如果这是有意为之，请在您的类型中添加 '(callSuper=false)'”
    所以按照IDEA给出的建议添加了下面这一行注释*/
@EqualsAndHashCode(callSuper = true)
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
