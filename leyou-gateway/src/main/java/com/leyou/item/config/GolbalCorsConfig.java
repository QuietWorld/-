package com.leyou.item.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *  全局的跨域问题解决配置
 * @author zc
 */
@Configuration
@EnableConfigurationProperties(value = CorsProperties.class)
public class GolbalCorsConfig {

    @Autowired
    private CorsProperties corsProperties;

    @Bean
    public CorsFilter corsFilter(){
        // 第一步：添加Cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 允许访问的域名
        for (String origin : corsProperties.getAllowedOrigin()){
            config.addAllowedOrigin(origin);
        }
        // 是否发送Cookie信息
        config.setAllowCredentials(true);
        // 允许请求的方式
        for (String method : corsProperties.getAllowedMethod()){
            config.addAllowedMethod(method);
        }
        // 允许的头
        config.addAllowedHeader("*");

        // 第二步：配置拦截的路径(为那些资源进行Cors配置)
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);

        // 第三步：返回新的CorsFilter
        return new CorsFilter(configurationSource);
    }
}
