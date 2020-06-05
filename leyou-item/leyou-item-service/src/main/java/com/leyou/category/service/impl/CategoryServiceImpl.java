package com.leyou.category.service.impl;

import com.leyou.category.dao.CategoryDao;
import com.leyou.category.domain.Category;
import com.leyou.category.service.CategoryService;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LeyouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     *
     * @param pid 商品的父节点id
     * @return
     */
    @Override
    public List<Category> queryCategoryListByPid(Long pid) {
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
}
