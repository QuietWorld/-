package com.leyou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zc
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = {"com.leyou.item.category.dao","com.leyou.item.brand.dao","com.leyou.item.specification.dao",
                    "com.leyou.item.spu.dao"  })
public class LeyouItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouItemApplication.class, args);
    }
}
