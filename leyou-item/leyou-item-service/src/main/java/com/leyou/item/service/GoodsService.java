package com.leyou.item.service;

import com.leyou.item.domain.Sku;
import com.leyou.item.domain.Spu;
import com.leyou.item.domain.SpuDetail;
import com.leyou.item.rpo.GoodsPageRpo;
import com.leyou.item.vo.PageResult;
import com.leyou.item.vo.SpuVo;

import java.util.List;

/**
 * 商品业务层接口
 * @author zc
 */
public interface GoodsService {

    /**
     * 分页查询商品信息
     * @param goodsRpo
     * @return
     */
    PageResult<SpuVo> pageQueryGoods(GoodsPageRpo goodsRpo);

    /**
     * 保存商品
     * @param spu
     */
    void saveGoods(Spu spu);

    /**
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    SpuDetail getSpuDetailBySpuId(Long spuId);

    /**
     * 查询该spuId下的所有sku
     * @param spuId
     * @return
     */
    List<Sku> listSkusBySpuId(Long spuId);

    /**
     * 更新商品
     * @param spu
     */
    void updateGoods(Spu spu);
}
