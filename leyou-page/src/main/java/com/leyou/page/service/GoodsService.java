package com.leyou.page.service;

import java.util.Map;

/**
 * @author zc
 */
public interface GoodsService {

    /**
     * 根据spuId制作前台商品详情页模型数据
     * @param id spu的id
     * @return
     */
    Map<String, Object> createModel(Long id);

    /**
     * 创建或更新静态页
     * @param spuId
     */
    void createHtml(Long spuId);

    /**
     * 删除静态页
     * @param spuId
     */
    void deleteHtml(Long spuId);
}
