package com.local.springboot.zuul.zuulserver.filter;

import com.local.springboot.zuul.zuulserver.feign.SsoFeign;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义过滤器
 */
public class MyAccessFilter extends ZuulFilter {

    @Qualifier("ssoFeignFallback")
    @Autowired
    private SsoFeign feign;

    /**
     * 过滤器类型，可选值有 pre、route、post、error。
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 通过int值来定义过滤器的执行顺序
     * 过滤器的执行顺序，数值越小，优先级越高。
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行该过滤器，true 为执行，false 为不执行
     * 这个也可以利用配置中心来实现，达到动态的开启和关闭过滤器。
     * 配置文件中禁用过滤器：
     * 【zuul.过滤器的类名.过滤器类型.disable=true，如：zuul.MyAccessFilter.pre.disable=true】
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器具体逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        // 获取cookie里面的accessToken值
        String accessToken = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
            }
        }
        // 请求url地址
        String url = getUrl(request);
        // 过滤登录接口、登录页面、若带登录令牌，则验证令牌是否有效；有效则表示为登录用户
        if (url.contains("/sso-server/sso/toLogin") || url.contains("/sso-server/sso/login") ||
                ((StringUtils.isNotBlank(accessToken)) && true)) {
            // 标识为登录用户
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
        } else {
            // 标识为未登录用户，跳转至sso认证中心，并将自己的地址作为参数
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(302);
            try {
                response.sendRedirect("http://localhost:8088/sso-server/sso/toLogin?url=" + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getUrl(HttpServletRequest request) {
        // 请求url初始化
        StringBuilder url = new StringBuilder(request.getRequestURL().toString());

        // 请求方式
        String method = request.getMethod();
        if ("GET".equals(method)) {
            // GET请求拼接参数
            url.append("?");
            // 参数集合
            Map<String, String[]> parameterMap = request.getParameterMap();
            Object[] keys = parameterMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                // 参数名
                String key = (String) keys[i];
                // 参数值
                String value = parameterMap.get(key)[0];
                url.append(key).append("=").append(value).append("&");
            }
            url.delete(url.length() - 1, url.length());
        }
        return url.toString();
    }
}
