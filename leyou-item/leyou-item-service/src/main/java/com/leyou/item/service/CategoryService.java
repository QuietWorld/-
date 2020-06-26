package com.leyou.item.service;

import com.leyou.item.domain.Category;

import java.util.List;

/**
 *  商品分类的业务层接口
 * @author zc
 */
public interface CategoryService {

    /**
     * 根据父节点Id查询商品分类
     * @param pid 父节点Id
     * @return
     */
    List<Category> listCategoriesByPid(Long pid);

    /**
     * 保存商品分类
     * @param category
     */
    void saveCategory(Category category);

    /**
     * 根据分类id集合查询多个分类
     * @param ids 分类id集合
     * @return
     */
    List<Category> listCategoriesByIds(List<Long> ids);
}
