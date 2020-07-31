package com.leyou.gateway.filter;

import com.leyou.auth.common.util.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 扩展zuul提供的ZuulFilter对全局的请求进行过滤，判断用户是否已经登录，
 * 如果已经登录则允许路由，如果未登录则不允许路由。
 *
 * @author zc
 */
@Component
@EnableConfigurationProperties(value = {FilterProperties.class, JwtProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private FilterProperties filterProp;

    @Autowired
    private JwtProperties jwtProp;

    /**
     * 拦截类型，此处声明为前置拦截:即在zuul网关对请求路由之前进行拦截过滤
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 拦截优先级，返回的int类型值越小，拦截器的优先级越高，越先执行。
     * 返回的int类型值应该是便于扩展的。
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 10;
    }

    /**
     * 是否执行拦截方法（即下面的run方法），返回true时执行，返回false时不执行。
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 获取zuul的请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 获取HttpServletRequest
        HttpServletRequest request = requestContext.getRequest();
        String url = request.getRequestURL().toString();

        List<String> allowPaths = filterProp.getAllowPaths();
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url, allowPath)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对请求进行拦截后要做的事情在该方法里面定义。
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 获取zuul的请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 获取HttpServletRequest
        HttpServletRequest request = requestContext.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        if (StringUtils.isBlank(token)) {
            // token为空，校验失败，不对请求进行路由
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(403);
            return null;
        }
        try {
            // 解析token
            JwtUtils.getInfoFromToken(token, jwtProp.getPublicKey());
            // todo 校验成功后，还可以对用户的权限进行校验，以控制用户可以访问那些接口
            /*
            * 获取到userInfo后，可以根据userInfo查询用户的权限，根据权限查询用户可以访问的url地址，
            * 如果当前用户可以访问的url地址集合中包含了用户当前正在访问的url地址，那么就路由，否则就拦截。
            * */
        } catch (Exception e) {
            // 解析token失败，不对请求进行路由
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(403);
        }
        return null;
    }
}
