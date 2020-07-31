package com.leyou.user.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zc
 */
@Data
@ConfigurationProperties(prefix = "leyou.user")
public class UserProperties {


    private String exchange;
    private String routingKey;
    /**
     * 保存到redis中key的有效时长
     */
    private Long effectiveDuration;
}
