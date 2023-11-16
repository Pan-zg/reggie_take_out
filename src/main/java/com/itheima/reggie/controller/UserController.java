package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        // 1.获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {

            // 2.生成4位随机数验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 日志，测试用
            log.info("生成的验证码为：{}", code);

            // 3.调用阿里云提供的短信服务api完成发送短信功能
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);
            /*由于该签名需要审核，但个人开发者不具备条件，故先做伪*/

            // 4.将生成的验证码存储到Session中
            session.setAttribute(phone, code);

            return R.success("发送短信验证码成功");
        }

        return R.error("发送短信验证码失败");
    }// 发送短信验证码功能 代码结束


    /**
     * 用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map/*由于需要提交user中的手机号和验证码，user中没有验证码，故使用map类型*/ map,
                           HttpSession session) {

        log.info(map.toString());

        // 1.获取手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        // 2.从Session中获取之前存储的验证码
        Object codeInSession = session.getAttribute(phone);

        // 3.判断验证码是否正确
        if (codeInSession != null && codeInSession.toString().equals(code)) {

            // 4.如果验证码正确，判断用户是否是第一次登录
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);
            if (user == null) {
                // 5.如果是第一次登录，自动创建用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            // 6.将用户信息存储到Session中
            session.setAttribute("user", user.getId());

            return R.success(user);
        }

        return R.error("登陆失败");
    }// 用户登录方法 代码结束
}
