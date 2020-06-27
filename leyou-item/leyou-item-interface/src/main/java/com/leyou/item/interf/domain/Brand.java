package com.leyou.item.interf.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 品牌实体类
 *
 * @author zc
 */
@Table(name = "tb_brand")
@Data
public class Brand {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    /**
     * 品牌log的url
     */
    private String image;
    /**
     * 品牌的首字母
     */
    private Character letter;

}
