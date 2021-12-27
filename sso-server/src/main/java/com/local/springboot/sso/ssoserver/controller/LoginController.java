package com.local.springboot.sso.ssoserver.controller;

import com.local.springboot.sso.ssoserver.util.AuthUtil;
import com.local.springboot.sso.ssoserver.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/sso")
@SuppressWarnings("all")
public class LoginController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 验证用户登录令牌是否有效
     *
     * @param accessToken 登录令牌
     * @return true 有效、false 无效
     */
    @PostMapping("/checkAccessToken/{accessToken}")
    public boolean checkAccessToken(@PathVariable("accessToken") String accessToken) {
        return authUtil.checkAccessToken(accessToken);
    }

    /**
     * 用户登录界面
     *
     * @param url
     * @return
     */
    @RequestMapping("/toLogin")
    public ModelAndView toLogin(String url) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("url", url);
        return modelAndView;
    }

    /**
     * 用户认证登录
     *
     * @param response HttpServletResponse
     * @param userName 用户
     * @param password 密码
     * @param url      服务器请求url
     * @return 认证结果、重定向请求
     */
    @RequestMapping("/login")
    public String login(HttpServletResponse response, String userName, String password, String url) {
        // 用户认证，并生成Token
        String accessToken = authUtil.checkUser(userName, password);
        if (StringUtils.isNotBlank(accessToken)) {
            try {
                // 与用户建立全局会话（将Token写到cookie中）
                Cookie cookie = new Cookie("accessToken", accessToken);
                cookie.setMaxAge(60 * 3);
                //设置访问路径
                cookie.setPath("/");
                response.addCookie(cookie);
                // 重定向请求
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "认证失败";
    }

//    @RequestMapping("/info")
//    public String get(){
//        //redisUtil.set("accessToken","12345");
//        String accessToken = (String)redisUtil.get("accessToken");
//        boolean flag = redisUtil.hasKey("accessToken");
//        if (StringUtils.isBlank(accessToken)) {
//            return "redis 异常";
//        }if (flag) {
//            return "redis 存在";
//        }
//        return accessToken;
//    }

    @GetMapping("/info/{id}")
    public String get(@PathVariable("id") String id) {
        return "成功调用:" + id;
    }
}
