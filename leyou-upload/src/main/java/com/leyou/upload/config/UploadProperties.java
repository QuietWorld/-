package com.leyou.upload.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 文件上传配置读取类
 * @author zc
 */
@Configuration
@ConfigurationProperties(prefix = "leyou.upload")
@Data
public class UploadProperties {

    /**
     * 允许上传的文件类型
     */
    private List<String> allowedType;
    /**
     * 返回的url的前置域名，如：http://image.leyou.com
     */
    private List<String> baseUrl;
}
