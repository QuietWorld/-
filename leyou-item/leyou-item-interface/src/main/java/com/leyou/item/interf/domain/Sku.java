package com.leyou.item.interf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * tb_sku实体类
 * Sku是库存量单位，代表了具体的一件商品。
 * @author zc
 */
@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    /**
     * 商品特殊规格的键值对
     */
    private String ownSpec;
    /**
     *  商品特殊规格的下标
     */
    private String indexes;
    /**
     * 是否有效，逻辑删除用
     */
    private Boolean enable;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date lastUpdateTime;
    /**
     * 库存,通用Mapper查询时忽略该属性
     */
    @Transient
    private Long stock;
}