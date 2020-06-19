package com.leyou.item.specification.dao;

import com.leyou.item.domain.SpecGroup;
import tk.mybatis.mapper.common.Mapper;

/**
 * 商品规格参数组持久层接口，继承通用Mapper进行单表CRUD
 * @author zc
 */
public interface SpecGroupDao extends Mapper<SpecGroup> {
}
