package com.leyou.auth.test;


import com.leyou.auth.common.pojo.UserInfo;
import com.leyou.auth.common.util.JwtUtils;
import com.leyou.auth.common.util.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: 98050
 * @Time: 2018-10-23 20:58
 * @Feature: JWT测试
 */
public class JwtTest {

    private static final String pubKeyPath = "D:\\javaother\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\javaother\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    /**
     * 生成公钥和私钥
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        // secret越复杂越安全,各种字符都来点
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 使用私钥对token加密并生成jwt类型的token
     * @throws Exception
     */
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    /**
     * 使用公钥对jwt类型的token进行解密
     * @throws Exception
     */
    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU5NDM5MDcwMn0.be2ubwGmYhZA223T9W8iymm8RAq-B3lnHq4UbgcCiKGJm-duAe96d5VZBK6SahXWdzsYIb6TxOhUZIKAagS-I7Gt_PKPEs2EMgi6vp_WUiyC7LZrYKm9h8UPRzD4rsjP0ZK2Wh45-F6sXgl8pYNEQvqx5tiN0cI_ApVkWRpQKao";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}