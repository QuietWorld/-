package com.leyou.item.service.dao;

import com.leyou.item.interf.domain.Spu;
import tk.mybatis.mapper.common.Mapper;

/**
 * Spu表持久层接口，继承通用Mapper进行单表CRUD
 * @author zc
 */
public interface SpuDao extends Mapper<Spu> {
}
