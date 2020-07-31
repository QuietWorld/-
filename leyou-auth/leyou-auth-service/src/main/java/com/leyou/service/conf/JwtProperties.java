package com.leyou.service.conf;


import com.leyou.auth.common.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author zc
 */
@ConfigurationProperties(prefix = "leyou.jwt")
@Data
@Slf4j
public class JwtProperties {

    /**
     * 公钥路径
     */
    private String publicKeyPath;

    /**
     * 私钥路径
     */
    private String privateKeyPath;

    /**
     * token有效时间
     */
    private int expireMinutes;

    /**
     * cookie名
     */
    private String cookieName;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * 公钥
     */
    private PublicKey publicKey;





    /**
     * 初始化privateKey和publicKey
     */
    @PostConstruct // @PostConstruct 在构造方法执行之后执行
    private void init() {
        File privateKeyFile = new File(privateKeyPath);
        File publicKeyFile = new File(publicKeyPath);
        // 公钥和私钥不存在，生成公钥和私钥
        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            try {
                RsaUtils.generateKey(publicKeyPath, privateKeyPath, secret);
            } catch (Exception e) {
                log.error("[RSA] 生成生成公钥和私钥失败，publicKeyFileName:{}，privateKeyFileName:{}", publicKeyPath, privateKeyPath, e);
                throw new RuntimeException();
            }
        }
        // 公钥和私钥存在，直接获取
        try {
            this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        } catch (Exception e) {
            log.error("[RSA] 获取公钥失败，publicKeyFileName:{}", publicKeyPath, e);
            throw new RuntimeException();
        }
        try {
            this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        } catch (Exception e) {
            log.error("[RSA] 获取私钥失败，privateKeyFileName:{}", privateKeyPath, e);
            throw new RuntimeException();
        }
    }
}
