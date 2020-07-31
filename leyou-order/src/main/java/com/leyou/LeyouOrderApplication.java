package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author zc
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeyouOrderApplication {

    public static void main(String[] args){
        SpringApplication.run(LeyouOrderApplication.class, args);
    }
}
