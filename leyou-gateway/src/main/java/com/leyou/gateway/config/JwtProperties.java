package com.leyou.gateway.config;

import com.leyou.auth.common.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "leyou.jwt")
@Data
@Slf4j
public class JwtProperties {


    private String cookieName;

    /**
     * 公钥保存路径
     */
    private String publicKeyPath;

    private PublicKey publicKey;


    /**
     * 初始化publicKey
     * @PostConstruct 该注解的方法在当前类构造方法执行之后执行
     */
    @PostConstruct
    private void init() {
        try {
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            log.error("[RSA] 获取公钥失败，publicKeyPath:{}", publicKeyPath, e);
            throw new RuntimeException();
        }
    }
}
