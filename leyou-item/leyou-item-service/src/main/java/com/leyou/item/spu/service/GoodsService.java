package com.leyou.item.spu.service;

import com.leyou.item.domain.Spu;
import com.leyou.item.spu.rpo.GoodsRpo;
import com.leyou.item.spu.vo.SpuVo;
import com.leyou.item.vo.PageResult;

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
    PageResult<SpuVo> pageQueryGoods(GoodsRpo goodsRpo);
}
