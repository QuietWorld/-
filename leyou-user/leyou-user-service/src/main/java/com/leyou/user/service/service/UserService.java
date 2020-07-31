package com.leyou.user.service.service;

import com.leyou.user.interf.domain.User;

public interface UserService {

    /**
     * 实现对用户提交的数据进行校验，主要包括对：手机号，用户名的唯一校验
     * @param data  要检验的数据
     * @param type  要检验的数据类型 1.用户名 2.手机号
     * @return true:可用 false：不可用
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送短信验证码
     * @param phone 手机号码
     */
    void sendVerifyCode(String phone);

    /**
     * 用户注册
     *
     * @param user
     * @param code
     */
    void register(User user, String code);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User getUser(String username, String password);
}
