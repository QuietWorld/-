package com.leyou.service.service.impl;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.util.JwtUtils;
import com.leyou.service.client.UserClient;
import com.leyou.service.conf.JwtProperties;
import com.leyou.service.service.AuthService;
import com.leyou.user.interf.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author zc
 */

@Service
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties authProp;


    /**
     * 登录认证，并生成token
     *
     * @param username
     * @param password
     * @return @return 使用rsa私钥加密后的jwt类型的token
     */
    @Override
    public String authorize(String username, String password) {
        // 根据用户名和密码查询用户
        User user = userClient.getUser(username, password);
        if (user == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        // 获取生成token
        String token = null;
        try {
            token = JwtUtils.generateToken(userInfo, authProp.getPrivateKey(), authProp.getExpireMinutes());
        } catch (Exception e) {
            log.warn("[授权中心] 生成token失败", e);
        }
        // 返回token
        return token;
    }

    /**
     * 根据携带的token凭证对用户是否已经登录进行校验
     *
     * @param token
     * @return
     */
    @Override
    public UserInfo verify(String token) {
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, authProp.getPublicKey());
            if (userInfo != null){
                return userInfo;
            }
        } catch (Exception e) {
            log.warn("[授权中心] token校验失败");
        }
        return null;
    }
}
