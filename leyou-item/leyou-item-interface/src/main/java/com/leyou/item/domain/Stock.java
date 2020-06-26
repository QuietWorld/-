package com.leyou.item.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;


/**
 * tb_stock库存表实体类，每个库存对应的都是一个具体商品，所以主键是skuId
 * 为什么库存不直接和sku表设计到一起，而是单独作为一张表？
 * 答：因为商品的库存通常是时刻变化的，读写的频率极其的高，而商品的其他信息通常改变并不频繁，所以
 * 我们查询库存的时候没必要将商品的其他信息都查出来。将库存单独作为一场表可以提高库存读写的效率，也是数据库表的垂直拆分。
 */
@Data
@Table(name = "tb_stock")
public class Stock {

    @Id
    private Long skuId;
    /**
     * 秒杀可用库存
     */
    private Long seckillStock;
    /**
     * 已秒杀数量
     */
    private Long seckillTotal;
    /**
     * 正常库存
     */
    private Long stock;
}