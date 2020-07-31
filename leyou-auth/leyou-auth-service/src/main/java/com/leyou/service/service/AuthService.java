package com.leyou.service.service;

import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.user.interf.domain.User;

/**
 * @author zc
 */

public interface AuthService {

    /**
     * 登录认证，并生成token
     * @param username
     * @param password
     * @return @return 使用rsa私钥加密后的jwt类型的token
     */
    String authorize(String username, String password);

    /**
     * 根据携带的token凭证对用户是否已经登录进行校验
     * @param token
     * @return
     */
    UserInfo verify(String token);
}
