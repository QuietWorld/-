package com.leyou.interceptor;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.util.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.conf.JwtProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;

/**
 * 对购物车服务的所有请求都进行拦截，拦截后解析请求携带的token获取载荷信息（UserInfo），
 * 并将UserInfo信息设置到ThreadLocal<UserInfo>中
 *
 * @author
 */
public class UserInterceptor implements HandlerInterceptor {


    private JwtProperties jwtProp;

    public UserInterceptor(JwtProperties jwtProp) {
        this.jwtProp = jwtProp;
    }

    /**
     * ThreadLocal的本质是一个Map集合，key是当前线程，value是要往ThreadLocal中存入的数据。
     * ThreadLocal称为线程域，顾名思义，每个线程都只能从线程域中取出自己的数据。
     */
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1.获取token
        String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        if (StringUtils.isBlank(token)) {
            // 未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            // 2.解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey());
            // 3.将userInfo存入ThreadLocal域中
            tl.set(userInfo);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    /**
     * 在页面成功渲染后，删除绑定到ThreadLocal的userInfo，保证thread是无状态的。
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tl.remove();
    }

    /**
     * 从ThreadLocal域中取出当前线程的userInfo
     *
     * @return
     */
    public static UserInfo getLoginUser() {
        return tl.get();
    }
}
