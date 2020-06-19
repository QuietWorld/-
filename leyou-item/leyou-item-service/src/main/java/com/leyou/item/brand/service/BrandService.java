package com.leyou.item.brand.service;

import com.leyou.item.brand.rpo.PageParam;
import com.leyou.item.domain.Brand;
import com.leyou.item.vo.PageResult;

import java.util.List;

/**
 * @author zc
 */
public interface BrandService {

    /**
     * 分页查询品牌
     * @param rpb
     * @return
     */
    PageResult<Brand> pageQuery(PageParam rpb);

    /**
     * 保存品牌
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 更新品牌
     * @param brand
     * @param cids
     */
    void updateBrand(Brand brand, List<Long> cids);

    /**
     * 根据品牌id获取品牌名称
     * @param id
     * @return
     */
    Brand getBrandById(Long id);
}
