package com.leyou.service.web;


import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.util.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.service.conf.JwtProperties;
import com.leyou.service.service.AuthService;
import com.leyou.user.interf.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 授权中心：
 * 1.对用户进行授权和鉴权
 * 2.对服务进行授权和鉴权（没做，当前只做了1）
 * --- 对服务的授权是很有必要的，
 *
 * @author zc
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProp;

    /**
     * 登录授权,并生成token
     *
     * @param username 用户名
     * @param password 密码
     * @return 使用rsa私钥加密后的jwt类型的token
     */
    @PostMapping("/accredit")
    public ResponseEntity<String> authorize(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        String token = authService.authorize(username, password);
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request, response, jwtProp.getCookieName(), token, jwtProp.getExpireMinutes() * 60);
        return ResponseEntity.ok(token);
    }

    /**
     * 检验用户的登录状态（鉴权）
     *
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        // 判断token中的是否存在有效的用户信息
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserInfo userInfo = authService.verify(token);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // 重新生成token用以刷新token的有效时间
            token = JwtUtils.generateToken(userInfo, jwtProp.getPrivateKey(), jwtProp.getExpireMinutes());
        } catch (Exception e) {
            log.warn("[授权中心] 重新生成token失败", e);
        }
        // 重新刷新cookie的有效时间，并将重新生成的token设置到cookie中并响应给客户端浏览器
        CookieUtils.setCookie(request, response, jwtProp.getCookieName(), token, jwtProp.getExpireMinutes() * 60);
        return ResponseEntity.ok(userInfo);
    }
}
