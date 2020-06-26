package com.leyou.item.dao;

import com.leyou.item.domain.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

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

    /**
     * -- 下面的三个语句实现功能。
     * -- 我的需求是根据分类id查询出品牌，而分类和品牌之间的关系在中间表中维护，品牌的信息在品牌表中维护。
     * -- 所以我们需要到两张表中去查询，而使用显示内连接和外连接就可以将多张表连接起来进行多表查询，为了
     * -- 消除多表查询的笛卡尔积情况，所以我们使用on消除笛卡尔积，然后通过where选择分类下的id；
     * 注意：子查询的效率要低于其他两种！！！
     * -- 子查询
     * select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = '76');
     * -- 显示内连接查询
     * select * from tb_brand b inner join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = 76;
     * -- 左外查询
     * select * from tb_brand b left outer join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = 76;
     *
     * @param cid 分类id
     * @return
     */
    @Select("select b.id, b.name from tb_brand b inner join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> listBrandsByCid(@Param("cid") Long cid);
}
