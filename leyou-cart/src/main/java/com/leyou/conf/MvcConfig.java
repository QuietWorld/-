package com.leyou.conf;

import com.leyou.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProp;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Spring的对对象进行依赖注入前提是由Spring来创建该对象，这里我们通过的new的方式来创建UserInterceptor对象
        // 那么该对象中的@Autowired将不起作用，所以我们应该换一种方式来初始化UserInterceptor对象的JwtProperties属性。
        registry.addInterceptor(new UserInterceptor(jwtProp)).addPathPatterns("/**");
    }
}
