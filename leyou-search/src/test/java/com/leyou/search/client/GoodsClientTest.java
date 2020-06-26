package com.leyou.search.client;

import com.leyou.item.domain.Sku;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsClientTest {

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void listSkusBySpuId() {
        List<Sku> skus = goodsClient.listSkusBySpuId(16L);
        skus.forEach(System.out::println);
    }
}