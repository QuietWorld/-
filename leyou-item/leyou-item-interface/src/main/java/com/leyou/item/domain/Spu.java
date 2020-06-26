package com.leyou.item.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Spu是Sku的集合，表示了具有相同特征的一组Sku集合，是抽象的概念。
 * @author zc
 */
@Table(name = "tb_spu")
@Data
public class Spu {

    /** SpuID，统一管理整个Spu下的所有Sku */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    /** 一级类目 */
    private Long cid1;
    /** 二级类目 */
    private Long cid2;
    /** 三级类目 */
    private Long cid3;
    /** 标题 */
    private String title;
    /** 子标题 */
    private String subTitle;
    /** 是否上架 */
    private Boolean saleable;
    /** 是否有效，逻辑删除用 */
    private Boolean valid;
    /** 创建时间 */
    private Date createTime;
    /** 最后修改时间*/
    private Date lastUpdateTime;

    /**
     * Sku集合，使用@Transient声明该属性不属于tb_spu表字段
     */
    @Transient
    private List<Sku> skus;
    /**
     * 将spu表进行垂直拆分，将可能会保存文本的字段单独拆分出来作为tb_spuDetail表，提高spu表的查询效率，使用@Transient
     * 声明该属性属于spu表字段。
     */
    @Transient
    private SpuDetail spuDetail;
}