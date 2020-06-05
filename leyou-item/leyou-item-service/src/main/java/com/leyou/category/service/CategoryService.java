package com.leyou.category.service;

import com.leyou.category.domain.Category;

import java.util.List;

/**
 *  商品分类的业务层接口
 * @author zc
 */
public interface CategoryService {

    List<Category> queryCategoryListByPid(Long pid);
}
