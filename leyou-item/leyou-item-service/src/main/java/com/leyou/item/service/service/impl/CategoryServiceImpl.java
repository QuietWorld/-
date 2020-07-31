package com.leyou.item.service.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.item.interf.domain.Category;
import com.leyou.item.service.dao.CategoryDao;
import com.leyou.item.service.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *  商品分类的业务层实现类
 * @author zc
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * 根据父节点id查询商品
     *
     * @param pid 商品的父节点id
     * @return
     */
    @Override
    public List<Category> listCategoriesByPid(Long pid) {
        // 通用mapper会将对象的非空属性作为查询条件
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categories = categoryDao.select(category);
        // 阿里巴巴规范：调用任何放回对象的方法，都应该对返回的对象进行非空判断,防止NPE
        // if (categories == null && categories.isEmpty()){}
        if (CollectionUtils.isEmpty(categories)){
            throw new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categories;
    }

    /**
     * 保存商品分类
     *
     * @param category
     */
    @Transactional(rollbackFor = {Error.class, RuntimeException.class})
    @Override
    public void saveCategory(Category category) {
        category.setId(null);
        int count = categoryDao.insert(category);
        if (count != 1){
            log.warn("商品分类保存失败:{}",category.toString());
            throw new LeyouException(ExceptionEnum.CATEGORY_SAVE_ERROR);
        }
    }

    /**
     * 根据分类id集合查询多个分类
     * @param cids 分类id集合
     * @return
     */
    @Override
    public List<Category> listCategoriesByIds(List<Long> cids){
        List<Category> categories = categoryDao.selectByIdList(cids);
        if (CollectionUtils.isEmpty(categories)){
            throw new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categories;
    }

    /**
     * 根据分类id查询分类
     * @param id 分类id
     * @return
     */
    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryDao.selectByPrimaryKey(id);
        if (category == null){
            throw new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return category;
    }
}
