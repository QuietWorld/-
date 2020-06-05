package com.leyou.brand.dao;

import com.leyou.category.domain.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 *  品牌查询接口
 * @author zc
 */
public interface BrandDao extends Mapper<Brand> {

    @Select("select * from tb_brand order by #{orderBy} #{desc} ")
    List<Brand> findAll(String orderBy, String desc);
}
