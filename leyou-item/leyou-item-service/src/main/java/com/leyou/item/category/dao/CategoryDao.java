package com.leyou.item.category.dao;

import com.leyou.item.domain.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zc
 * 商品分类操作接口,继承通用Mapper接口。单表的查询语句不再需要手写。
 * IdListMapper接口接收两个泛型T和PK
 * T:实体类
 * PK（Primary Key）：主键类型
 */

public interface CategoryDao extends Mapper<Category>, IdListMapper<Category, Long> {


}
