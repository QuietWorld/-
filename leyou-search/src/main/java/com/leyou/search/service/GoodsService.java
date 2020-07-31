package com.leyou.search.service;


import com.leyou.item.interf.domain.Spu;
import com.leyou.search.domain.Goods;

/**
 * 商品服务service层接口
 * @Author zc
 */
public interface GoodsService {

    /**
     *  根据给定的Spu对象构建Goods对象并返回
     * @param spu spu对象，包含了多个sku
     * @return
     */
    Goods buildGoods(Spu spu);

    /**
     * 新增或者修改商品
     * @param spuId
     */
    void insertOrUpdateIndex(Long spuId);

    /**
     * 删除索引库中指定id的商品
     * @param spuId
     */
    void deleteIndex(Long spuId);
}
