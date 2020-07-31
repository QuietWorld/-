package com.leyou.search.mq;


import com.leyou.search.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品消息监听
 * @author zc
 */
@Component
@Slf4j
public class ItemListener {

    @Autowired
    private GoodsService goodsService;



    /**
     * 数据同步: 向索引库新增或修改指定id的商品
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.search.insert.queue",durable = "true"),
            exchange = @Exchange(name = "leyou.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenInsert(Long spuId){
        if (spuId == null){
            return;
        }
       goodsService.insertOrUpdateIndex(spuId);
    }

    /**
     * 数据同步: 删除索引库中指定id的商品
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.search.delete.queue",durable = "true"),
            exchange = @Exchange(name = "leyou.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId){
        if (spuId == null){
            return;
        }
        goodsService.deleteIndex(spuId);
    }
}
