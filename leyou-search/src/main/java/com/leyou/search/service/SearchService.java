package com.leyou.search.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.rpo.SearchPageRpo;
import com.leyou.search.domain.Goods;

/**
 * 前台查询服务接口
 * @author zc
 */
public interface SearchService {

    /**
     * 根据关键字进行分页查询
     *
     * @param searchPageRpo
     * @return
     */
    PageResult<Goods> pageQueryGoods(SearchPageRpo searchPageRpo);
}
