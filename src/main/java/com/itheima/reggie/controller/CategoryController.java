package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 菜品/套餐的分类控制类
 * Create on 2023/05/15
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类方法
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        /**
         * 代码执行过程：
         * 1.页面（backend/page/category/list.html）发送ajax请求，将新增分类窗口输入的数据以json形式提交到服务器
         * 2.服务端Controller接收页面提交的数据并调用Service将数据进行保存
         * 3.Service调用Mapper操作数据库，保存数据
         */

        // 日志
        log.info("category:{}",category);   // 这一步传入的内容还不包含id

        categoryService.save(category);     /*在这一步才生成id，猜测是调用的Service方法生成的（也可能是数据库用
        auto_increment生成？）*/
        return R.success("新增分类成功！");

    }// 新增分类代码结束


    /**
     * 菜品/套餐分类的分页查询方法
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        /**
         * 代码执行过程分析：
         * 1.页面发送ajax请求，将分页参数（page、pageSize）提交到服务端
         * 2.服务端Controller接收页面提交的数据并调用Service查询数据
         * 3.Service调用Mapper操作数据库，查询分页数据
         * 4.Controller将查询到的分页数据响应给页面
         * 5.页面接收分页数据并通过ElementUI的Table组件展示到页面上
         */
        // 分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 条件构造器（以sort为条件）
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        // 进行分页查询
        categoryService.page(pageInfo);
        // 返回结果
        return R.success(pageInfo);

    }// 分类的分页查询方法代码结束


    /**
     * 删除分类方法
     * Create on 2023/05/16
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids/*注意，此处不是id而是ids，还需要加上@RequestParam注释 2023/05/16*/) {

        // 日志
        log.info("删除分类的id为：{}", ids);

        // 直接调用Service方法删除对应id的分类信息
        //categoryService.removeById(ids);
        // 先进行关联判断，再删除对应id分类
        categoryService.remove(ids);

        return R.success("分类信息删除成功！");

    }// 删除分类方法代码结束


    /**
     * 跟据id更新分类方法
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {

        // 日志
        log.info("更新分类信息：{}", category);

        // 直接调用updateById方法 并返回结果
        categoryService.updateById(category);
        return R.success("更新分类信息成功");

    }// 更新分类信息代码结束


    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {

        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        // 添加同否条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        // 获得并返回结果
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

    }// 条件查询分类数据方法代码结束
}
