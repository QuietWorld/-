package com.leyou.item.dao;

import com.leyou.item.domain.Sku;
import com.leyou.item.mapper.BaseMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * tb_sku表持久层接口
 * @author zc
 */
public interface SkuDao extends BaseMapper<Sku, Long> {
}
