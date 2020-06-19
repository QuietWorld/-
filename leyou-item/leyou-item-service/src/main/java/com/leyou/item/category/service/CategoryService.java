package com.leyou.item.category.service;

import com.leyou.item.domain.Category;

import java.util.List;

/**
 *  商品分类的业务层接口
 * @author zc
 */
public interface CategoryService {

    /**
     * 查询商品分类
     * @param pid
     * @return
     */
    List<Category> queryCategoryListByPid(Long pid);

    /**
     * 保存商品分类
     * @param category
     */
    void saveCategory(Category category);

    /**
     * 根据分类di查询分类
     * @param cids
     * @return
     */
    List<Category> getCategoryNameById(List<Long> cids);
}
