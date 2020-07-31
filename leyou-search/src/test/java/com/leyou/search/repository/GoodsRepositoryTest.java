package com.leyou.search.repository;

import com.leyou.common.exception.LeyouException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Spu;
import com.leyou.item.interf.domain.SpuDetail;
import com.leyou.item.interf.rpo.GoodsPageRpo;
import com.leyou.item.interf.vo.SpuVo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.domain.Goods;
import com.leyou.search.service.impl.GoodsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsRepositoryTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsServiceImpl goodsServiceImpl;


    @Test
    public void createIndex(){
        //
     esTemplate.createIndex(Goods.class);
     esTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        List<Spu> spus = goodsClient.listAllSpus();
        List<Goods> goodsList = null;
        for (Spu spu : spus){
            Goods goods = null;
            try {
                goods = goodsServiceImpl.buildGoods(spu);
            } catch (LeyouException e) {
                System.out.println(e.getMessage());
            }
            goodsList.add(goods);
        }
        goodsRepository.saveAll(goodsList);
    }

    @Test
    public void test1(){
        SpuDetail spuDetail = goodsClient.getSpuDetailBySpuId(206L);
        System.out.println(spuDetail.toString());
    }
}