package com.leyou.item.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * GolbalCorsConfig类的属性读取类
 * @author zc
 */
@ConfigurationProperties(prefix = "leyou.cors")
@Data
public class CorsProperties {

    /**
     * 允许访问的域
     */
    private List<String> allowedOrigin;

    /**
     * 允许请求的方式
     */
    private List<String> allowedMethod;

}
