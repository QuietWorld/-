package com.leyou.item.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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


}