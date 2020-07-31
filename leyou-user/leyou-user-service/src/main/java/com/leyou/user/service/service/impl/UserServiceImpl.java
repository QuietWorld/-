package com.leyou.user.service.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.interf.domain.User;
import com.leyou.user.service.config.UserProperties;
import com.leyou.user.service.dao.UserDao;
import com.leyou.user.service.service.UserService;
import com.leyou.user.service.util.CodecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableConfigurationProperties(UserProperties.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserProperties userProperties;

    private static final String KEY_PREFIX = "user:verify:phone:";

    /**
     * 实现对用户提交的数据进行校验，主要包括对：手机号，用户名的唯一校验
     *
     * @param data 要检验的数据
     * @param type 要检验的数据类型 1.用户名 2.手机号
     * @return true:可用 false：不可用
     */
    @Override
    public Boolean checkData(String data, Integer type) {
        // 当return一个boolean值的时候，千万不要判断true返回true，太业余了
        User user = new User();
        if (type == 1) {
            // 校验用户名
            user.setUsername(data);
            return userDao.selectCount(user) == 0;
        } else if (type == 2) {
            // 校验手机号
            user.setPhone(data);
            return userDao.selectCount(user) == 0;
        } else {
            throw new LeyouException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号码
     */
    @Override
    public void sendVerifyCode(String phone) {
        String key = KEY_PREFIX + phone;
        // 生成随机6位数
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        // 发送消息给消息微服务,让消息微服务发送验证码(由于rabbitmq安装失败所以暂时注释掉)
        // amqpTemplate.convertAndSend(userProperties.getExchange(),userProperties.getRoutingKey(),msg);
        // 将验证码保存到redis中并给定key的有效时长
        redisTemplate.opsForValue().set(key, code, userProperties.getEffectiveDuration(), TimeUnit.SECONDS);
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     */
    @Override
    public void register(User user, String code) {
        // 1.校验验证码
        String key = KEY_PREFIX + user.getPhone();
        String redisCode = redisTemplate.opsForValue().get(key);
        if (redisCode == null) {
            throw new LeyouException(ExceptionEnum.VERIFICATION_CODE_HAS_EXPIRED);
        }
        if (!StringUtils.equals(redisCode, code)) {
            return;
        }
        // 2.密码加密
        String passwordBcryptEncode = CodecUtils.passwordBcryptEncode(user.getUsername(), user.getPassword());
        user.setPassword(passwordBcryptEncode);
        // 3.保存信息到数据库中,insertSelective()不会保存null值,会使用数据库默认值
        user.setId(null);
        user.setCreated(new Date());
        int insertCount = userDao.insertSelective(user);
        if (insertCount != 1) {
            log.info("[用户服务] 用户注册失败,参数:{}", user);
            throw new LeyouException(ExceptionEnum.USER_SAVE_ERROR);
        }
        // 4.删除redis中保存的验证码
        redisTemplate.delete(key);
    }


    /**
     * 根据用户名和密码查询用户另一个版本,也可以
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User getUser(String username, String password) {
        User u = new User();
        u.setUsername(username);
        User user = userDao.selectOne(u);
        if (user == null) {
            return null;
        }
        // 加密用户输入的密码
        if (CodecUtils.passwordConfirm(username + password, user.getPassword())) {
            user.setPassword(password);
            return user;
        }
        return null;
    }
}
