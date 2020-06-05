package com.leyou.category.dao;

import com.leyou.category.domain.Category;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zc
 * 商品分类操作接口,继承通用Mapper接口。单表的查询语句不再需要手写。
 */

public interface CategoryDao extends Mapper<Category> {
}
