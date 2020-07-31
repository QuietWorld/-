package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * sms配置属性读取类
 * @author zc
 */
@ConfigurationProperties(prefix = "leyou.sms")
@Data
public class SmsProperties {

    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;

    Long sms_min_interval_in_ms;
}
