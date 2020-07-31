package com.leyou.user.service.util;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CodecUtilsTest {

    String password = "123456";
    String username = "lisi";

    @org.junit.Test
    public void passwordBcryptEncode() {
        for (int i = 0; i < 10; i++) {
            System.out.println(CodecUtils.passwordBcryptEncode(username,password));
        }
    }

    @org.junit.Test
    public void passwordConfirm() {
        Boolean aBoolean = CodecUtils.passwordConfirm("123456", "$2a$10$Beiw4YG3KR36oofAaRmIB.b0WW8XH.olyN5kJAiXAZPA/kgdJaqS3W");
        System.out.println(aBoolean);
    }
}