package com.leyou.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.sms.config.SmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 发送短信工具类
 *
 * @author zc
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtils {

    @Autowired
    private SmsProperties prop;
    @Autowired
    private StringRedisTemplate template;

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    private static final String KEY_PREFIX = "sms_";

    static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    public SendSmsResponse sendSms(String phone, String code, String signName, String template) {
        // 作为mq的消费方,我们通常将异常直接抛出从而触发重试防止消失丢失,但是由于短信服务的限流问题(在一段时间内向同一手机
        // 发送短信的次数过多会触发限流导致账号暂时不可用),所以我们将异常直接try掉而不进行重试
        try {
            String key = KEY_PREFIX + phone;
            // 限流处理,从redis中取出sendSms
            if (!StringUtils.isEmpty(this.template.opsForValue().get(key))) {
                // 如果取出来不是null,说明key还没过期,时间间隔还没有满60s,直接return。
                return null;
            }
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                    prop.getAccessKeyId(), prop.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(template);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + code + "\"}");

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("123456");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            // 限流处理,记录当前号码当前次发送短信时间到redis中,并设置key的失效时间为60s
            this.template.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), prop.getSms_min_interval_in_ms(), TimeUnit.MILLISECONDS);

            logger.info("发送短信状态：{}", sendSmsResponse.getCode());
            logger.info("发送短信消息：{}", sendSmsResponse.getMessage());

            return sendSmsResponse;
        } catch (ClientException e) {
            logger.error("[短信服务] 发送短信异常,手机号码:{}", phone, e);
            return null;
        }
    }

}
