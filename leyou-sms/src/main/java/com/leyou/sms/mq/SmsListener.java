package com.leyou.sms.mq;

import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.util.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.Map;

/**
 * 监听并发送短信
 *
 * @author zc
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsProperties prop;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.verify.code.queue", durable = "true"),
            exchange = @Exchange(name = "leyou.sms.exchange", type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"
    ))
    public void sendSms(Map<String, String> msg) { // rabbitmq发送消息时只能发送一个,也就是只能有一个参数,所以使用一个map集合来封装电话号码和验证码
        // 消费方一定需要对接收到的消息进行合法性校验,对不合法的消息直接return
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if (phone == null || "".equals(phone)){
            return;
        }
        smsUtils.sendSms(phone, JsonUtils.serialize(msg), prop.getSignName(), prop.getVerifyCodeTemplate());
    }
}

