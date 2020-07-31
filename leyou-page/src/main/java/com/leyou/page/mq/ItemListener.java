package com.leyou.page.mq;

import com.leyou.page.service.GoodsService;
import com.leyou.page.web.GoodsController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 商品服务监听
 *
 * @author zc
 */

@Component
@Slf4j
public class ItemListener {

    @Autowired
    private GoodsService goodsService;

    /**
     * 数据同步：新增或更新静态页
     * <p>
     * 对于消费者来说，除非是编译器异常，而运行期的异常我们通常不处理，因为出了异常可以让SpringAmqp帮我们
     * 做重试，一旦我们try掉异常，就说明消息消费成功了，那么队列中的消息也就消失了。而如果我们不处理异常，当
     * 抛出异常时，SpringAmqp不会做ack确认，那么消息就不会丢失还会出发重试。
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.page.insert.queue", durable = "true"),
            exchange = @Exchange(name = "leyou.item.exchange", type = ExchangeTypes.TOPIC),
            key = {"item.update", "item.insert"}
    ))
    public void listenInsert(Long spuId) {
        // 消费者通常都需要对收到的消息进行合法判断，如果不合法直接return什么都不做。
        if (spuId == null) {
            return;
        }
        goodsService.createHtml(spuId);
    }

    /**
     * 删除静态页
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "leyou.page.delete.queue", durable = "true"),
            exchange = @Exchange(name = "leyou.item.exchange", type = ExchangeTypes.TOPIC),
            key = "item.delete"
    ))
    public void listenDelete(Long spuId) {
        goodsService.deleteHtml(spuId);
    }


}
