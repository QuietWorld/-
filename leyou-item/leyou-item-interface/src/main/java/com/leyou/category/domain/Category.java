package com.leyou.category.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  商品分类的ORM实体类
 * @author zc
 */
@Table(name="tb_category")
@Data
public class Category {

    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
    // getter和setter略(使用Lombok生成)
    // 注意isParent的getter和setter方法
}
