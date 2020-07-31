package com.leyou.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zc
 */
@Data
@ConfigurationProperties("leyou.filter")
public class FilterProperties {


    /**
     * 不需要过滤的路径
     */
    private List<String> allowPaths;

}
