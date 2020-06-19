package com.leyou.item.brand.dao;

import com.leyou.item.domain.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *  品牌查询接口
 *  IdListMapper接口接收两个泛型T和PK
 *  T:实体类
 *  PK（Primary Key）：主键类型
 * @author zc
 */
public interface BrandDao extends Mapper<Brand>, IdListMapper<Brand, Long> {



    /**
     * 向品牌和分类的中间表中插入数据，来保存品牌的商品的所属分类信息。
     * @param bid  品牌id
     * @param cid  分类id
     * @return
     */
    @Insert("insert into tb_category_brand values (#{bid}, #{cid})")
    Integer saveBrandCategory(@Param("bid") Integer bid, @Param("cid") Long cid);

    /**
     * 在品牌和分类的中间表中根据品牌的id更新该品牌商品分类的id
     * @param id 品牌的id
     * @param cid 品牌商品分类的id
     * @return
     */
    @Update("update tb_category_brand set category_id = #{cid} where brand_id = #{id}")
    int updateCategoryBrand(@Param("id") Integer id, @Param("cid")Long cid);
}
