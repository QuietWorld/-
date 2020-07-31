package com.leyou.item.interf.rpo;

import lombok.Data;

import java.util.Map;

/**
 * search分页查询实体类
 * @author zc
 */

@Data
public class SearchPageRpo {

    /**
     * 搜索条件
     */
    private String key;
    /**
     * 当前页码
     */
    private Integer page;
    /**
     * 每页显示的条目数
     * 前台页面每页显示的条目数一般都是固定的，因为我们不可能让用户来指定每页显示多少条，
     * 比如用户输入10000条，那么我们就要查询10000条吗？显然不合适，所以定义为常量。
     */
    private static final Integer rows = 20;
    /**
     * 排序的字段
     */
    private String orderBy;
    /**
     * 排序的方式
     * true:升序 ASC
     * false：降序 DESC
     */
    private Boolean order;

    /**
     * 规格参数和品牌分类过滤条件
     * key:规格参数名，或者品牌，分类，根据key作为字段，在字段中进行匹配过滤
     * value:规格参数值或品牌id，三级分类id
     */
    private Map<String, String> filter;

    public Integer getPage(){
        if (page == null || page <= 0){
            return 1;
        }
        return page;
    }

    public Integer getRows(){
        return rows;
    }
}
