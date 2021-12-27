package com.local.springboot.sso.ssoserver.util;

import com.local.springboot.sso.ssoserver.entity.TSysUserEntity;
import com.local.springboot.sso.ssoserver.serice.TSysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: hzl
 * @date: 2021-09-14 18:13
 * @Description: 用户认证工具类
 **/
@Component
@SuppressWarnings("all")
public class AuthUtil {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TSysUserService userService;

    /**
     * 验证用户名和密码；并返回登录令牌
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public String checkUser(String userName, String password) {
        String accessToken = "";
        // 判断用户名和密码是否正确
        TSysUserEntity entity = userService.getUserByName(userName);
        if (entity != null) {
            String dbPwd = entity.getPassword();
            String pwd = Md5Util.MD5(password);
            if (StringUtils.equals(dbPwd, pwd)) {
                // 用户名+时间戳加密生成登录令牌、存放redis
                String md5Str = userName + System.currentTimeMillis();
                accessToken = Md5Util.MD5(md5Str);
                // 登录令牌为key、存储用户信息(过期时间3分钟)
                redisUtil.set(accessToken, entity.getId(), 3 * 60);
            }
        }
        return accessToken;
    }

    public static void main(String[] args) {
        System.out.println(Md5Util.MD5("158111920@qq.com"));
    }
    /**
     * 验证用户登录令牌是否有效
     *
     * @param accessToken 登录令牌
     * @return true 有效、false 无效
     */
    public boolean checkAccessToken(String accessToken) {
        return redisUtil.hasKey(accessToken);
    }

    public static String getLoginUserId() {
        return null;
    }
}
