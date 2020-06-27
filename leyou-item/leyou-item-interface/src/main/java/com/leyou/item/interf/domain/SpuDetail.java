package com.leyou.item.interf.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;


/**
 *   SpuDetail表是对Spu进行垂直拆分而分离出来表。本身我们是可以将两张表合并成一张表进行设计的，但是由于
 * 商品的详情都是字符串，且数据较大，如果设计成一张表，那么这张表的查询效率会很低。所以我们进行了垂直拆分，
 * 这样可以提升Spu表中字段的查询效率。
 * @author zc
 */
@Table(name="tb_spu_detail")
@Data
public class SpuDetail {

    /**
     *     对应的SPU的id，绝不应该设计成自增长，是spu的id，因为这张表本身就是垂直拆分生成的表，本身来说这些字段
     * 也是数据spu的字段，所以主键应该是统一的spuId
     * */
    @Id
    private Long spuId;
    /** 商品描述 */
    private String description;
    /** 商品特殊规格的名称及可选值模板 */
    private String specialSpec;
    /** 商品的全局规格属性 */
    private String genericSpec;
    /** 包装清单 */
    private String packingList;
    /** 售后服务 */
    private String afterService;
}