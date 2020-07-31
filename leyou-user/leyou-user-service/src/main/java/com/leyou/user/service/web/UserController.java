package com.leyou.user.service.web;


import com.leyou.user.interf.domain.User;
import com.leyou.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户接口控制器
 *
 * @author zc
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 实现对用户提交的数据进行校验，主要包括对：手机号，用户名的唯一校验
     *
     * @param data 要检验的数据
     * @param type 要检验的数据类型 1.用户名 2.手机号
     * @return true:可用 false：不可用
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type) {
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
        userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> getUser(@RequestParam("username")String username,
                                        @RequestParam("password")String password){
        User user = userService.getUser(username, password);
        if (user == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
