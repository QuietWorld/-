package com.leyou.brand.service;

import com.leyou.brand.rpo.PageParam;
import com.leyou.category.domain.Brand;
import com.leyou.vo.PageResult;

import java.util.List;

/**
 * @author zc
 */
public interface BrandService {

    PageResult<Brand> pageQuery(PageParam rpb);
}
