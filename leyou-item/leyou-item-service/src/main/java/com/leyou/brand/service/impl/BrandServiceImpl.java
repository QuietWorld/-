package com.leyou.brand.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.brand.dao.BrandDao;
import com.leyou.brand.service.BrandService;
import com.leyou.brand.rpo.PageParam;
import com.leyou.category.domain.Brand;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LeyouException;
import com.leyou.vo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 *  商品业务
 * @author zc
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Override
    public PageResult<Brand> pageQuery(PageParam rpb) {
        // 分页
        PageHelper.startPage(rpb.getPage(), rpb.getRows());
        // 过滤  根据关键字搜索查询(模糊查询) select * from brand where name like "%xxx%" or letter = "xxx" order by id desc;
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(rpb.getKey())){
            // 定义模糊查询的标准，由于数据库表中商品首字母都是大写，所以使用toUpperCase()方法。
            example.createCriteria().orLike("name",rpb.getKey()).orEqualTo("letter",rpb.getKey().toUpperCase());
        }
        // 排序   如果传递的sortBy参数为empty("")或null则不排序
        if (StringUtils.isNotBlank(rpb.getSortBy())){
            // String orderByClause = "id desc";
            String orderByClause = rpb.getSortBy() + " " + (rpb.getDesc()? "DESC" : "ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Brand> brands = brandDao.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)){
            throw new LeyouException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        PageInfo pageInfo = new PageInfo(brands);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }
}
