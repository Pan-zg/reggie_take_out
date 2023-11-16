package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录检查，确认其是否已完成登录
 * Create on 2023/05/11
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j  // 加入日志进行结果输出
public class LoginCheckFilter implements Filter {

    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    /**
     * 对用户登录进行判断
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;   // 对servletRequest进行类型强转
        HttpServletResponse response = (HttpServletResponse) servletResponse;   // 对servletResponse进行类型强转
        /**
         * 过滤器处理逻辑：
         * 1.获取本次请求的url
         * 2.判断本次请求是否需要处理
         * 3.若无需处理，直接放行
         * 4.判断登录状态，若已登录，则直接放行
         * 5，若未登录，则返回未登录结果
         */
        // 1.获取URI
        String requestURI = request.getRequestURI();
        // 使用日志输出拦截url，便于查看
        log.info("拦截到请求：{}", requestURI);

        // 2.定义一个列表用于存放无需处理的URL
        String[] urls = new String[] {
                "/employee/login",    // 登录URL
                "/employee/logout",   // 退出URL
                "/backend/**",    // 后端静态资源
                "/front/**",    // 前端静态资源
                "/common/**",   // 测试上传下载文件用路径
                "/user/login",  // 移动端登录
                "/user/sendMsg" // 移动端发送验证码
        };

        // 3.判断路径是否需要处理
        boolean check = check(urls, requestURI);// 直接调用check方法

        // 4.若无需处理则直接放行
        if (check) {
            // 日志，功能同上
            log.info("本次请求{}无需处理",requestURI);
            // 以下两句为放行语句（不做操作直接过），下同
            filterChain.doFilter(request, response);
            return;
        }

        // 5.1 判断员工employee登录状态，已登录则放行
        if (request.getSession().getAttribute("employee") != null) {
            // 日志
            log.info("员工已登录，id为：{}",request.getSession().getAttribute("employee"));

            // 传入并保存登录用户的id
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            // 插入获取线程id的代码，用于查看当前线程的id（测试用，正常使用时请注释掉）
            //long id = Thread.currentThread().getId();
            //log.info("当前线程id：{}", id);

            filterChain.doFilter(request, response);
            return;
        }

        // 5.2 判断用户user登录状态，已登录则放行
        if (request.getSession().getAttribute("user") != null) {
            // 日志
            log.info("用户已登录，id为：{}",request.getSession().getAttribute("user"));

            // 传入并保存登录用户的id
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            // 插入获取线程id的代码，用于查看当前线程的id（测试用，正常使用时请注释掉）
            //long id = Thread.currentThread().getId();
            //log.info("当前线程id：{}", id);

            filterChain.doFilter(request, response);
            return;
        }

        // 6.未登录，返回未登录结果（通过输出流的方式向客户端响应数据）
        // 此处的msg要参考backend/request.js文件中响应拦截器部分的msg代码，否则可能导致返回错误
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        // 日志
        log.info("用户未登录");
        return;


        // 以下为测试代码，使用时请注释
        //log.info("拦截到请求：{}", request.getRequestURI());  // 使用了“{}”的格式化输出
        //filterChain.doFilter(request, response);
    }   // dofilter部分的代码结束


    /**
     * 路径匹配，检查本次请求是否放行
     * @param urls
     * @param requstURI
     * @return
     */
    public boolean check(String[] urls, String requstURI) {
        for (String url : urls) {   // 遍历urls列表
            boolean match = PATH_MATCHER.match(url, requstURI);  // 使用路径匹配器进行匹配
            if (match) {    // 若匹配成功，则返回true
                return true;
            }
        }

        return false;   // 若匹配失败，则返回false
    }
}
