package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


/**
 * Employee控制类
 * Create on 2023/05/10
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录 employee login 方法
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /**
         * 登录要求分析：
         * 1.将页面提交的密码password进行md5加密处理
         * 2.根据页面提交的用户名username查询数据库
         *  - 若没有查询到结果，则返回结果：登录失败
         *  - 若查询到则继续
         * 3.密码比对
         *  - 若不一致，则返回结果：登录失败
         *  - 若相同则继续
         * 4.查看员工状态
         *  - 若被禁用则返回结果：员工已被禁用
         *  - 若未被禁用则继续
         * 5.登录成功，将员工id存入session并返回结果：登录成功
         */
        // 将页面提交密码进行加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);    // (使用ctrl+alt+v可以快速新建对象)

        // 判断并返回未查询到对应用户名的登陆失败结果（若成功则默认继续）
        if (emp == null) {
            return R.error("未查询到该用户，登录失败！");
        }

        // 判断并返回密码不一致的登陆失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码不一致，登录失败！");
        }

        // 判断并返回员工被禁用的登录失败结果（0表示被禁用）
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用！");
        }

        // 将员工名写入session，并返回登录成功的结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

        //return null;
    }// 登录login方法代码结束


    /**
     * 员工退出 logout 方法
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        // 清理session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");

    }// 退出logout方法代码结束


    /**
     * 新增员工方法
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        /**
         * 代码执行过程分析：
         * 1.页面发送ajax请求，将新增员工页面中输入的数据以json的形式提交到服务端
         * 2.服务端controller接收页面的数据并调用Service将数据j进行保存
         * 3.Service调用Mapper操作数据库，保存数据
         */
        // 调用日志输出新增员工的信息，测试用
        log.info("新增的员工信息：{}",employee.toString());

        // 设置初始密码为”123456“，并进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 设置创建时间和更新时间，均为当前（创建时的）时间
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        // 设置创建者和更新者
        // 1.获得当前登录用户的id
        //Long empId = (Long) request.getSession().getAttribute("employee");

        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        // 调用employeeService的save方法保存employee
        employeeService.save(employee);

        // 返回结果
        return R.success("新增员工成功！");    // 此处的save方法继承自mybatis中的IService中的save方法

    }// 新增员工方法代码结束


    /**
     * 员工信息分页查询方法
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        /**
         * 代码执行过程分析：
         * 1.页面发送ajax请求，将分页参数（page、pageSize、name）提交到服务端
         * 2.服务端Controller接收页面提交的数据并调用Service查询数据
         * 3.Service调用Mapper操作数据库，查询分页数据
         * 4.Controller将查询到的分页数据响应给页面
         * 5.页面接受到分页数据并通过ElementUI的Table组件展示到页面上
         */
        log.info("page = {}, pageSize = {}, name = {}",page, pageSize, name);

        // 1.构造分页构造器（处理传入的page和pageSize变量）
        Page pageinfo = new Page(page, pageSize);

        // 2.构造条件构造器（处理可能传入的name变量）
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 3.添加一个过滤条件（当name不为空时才传入）
        queryWrapper.like(StringUtils.isNotEmpty(name)/*注意，此处的StringUtils是apache.common.lang包下的*/,
                          Employee::getName, name);
        // 4.添加排序条件（以更新时间降序排列）
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 5.执行查询
        employeeService.page(pageinfo, queryWrapper);
        // 6.返回结果
        return R.success(pageinfo);

    }// 员工信息分页查询方法代码结束


    /**
     * 根据id修改员工信息（启用/禁用）方法
     //* @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(/*HttpServletRequest request,*/ @RequestBody Employee employee) {
        /**
         * 代码执行过程分析：
         * 1.页面发送ajax请求，将参数（id、status）提交到服务端
         * 2.服务端Controller接收页面提交的数据并调用Service更新数据
         * 3.Service调用Mapper操作数据库
         */
        log.info(employee.toString());


        // 【注】以下注释部分的代码已由java对象序列化（JacksonObjectMapper类）并由公共字段填充器（MetaObjectHandler类）自动填充
        // 插入获取线程id的代码，用于查看当前线程的id（测试用，正常使用时请注释掉）
        //long id = Thread.currentThread().getId();
        //log.info("当前线程id：{}", id);

        // 获取当前修改者的信息
        //Long empId = (Long) request.getSession().getAttribute("employee");

        // 设置修改时间和员工信息
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);


        // 调用employeeService更新员工信息并返回R结果
        employeeService.updateById(employee);
        return R.success("员工信息修改成功！");

    }// 修改员工信息方法代码结束


    /**
     * 根据id查询员工信息方法
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {

        // 日志
        log.info("根据id查询员工信息...");

        // 调用Service方法获取id
        Employee employee = employeeService.getById(id);

        // 对employee进行判断，不为空则返回查询结果，否则报错
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("未查询到对应的员工信息！");

    }// 员工信息查询方法代码结束
}
