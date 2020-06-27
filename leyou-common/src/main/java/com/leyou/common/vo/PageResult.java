package com.leyou.common.vo;

import lombok.Data;

import java.util.List;

/**
 *  view object
 * 封装分页查询后数据的类
 * @author zc
 */
@Data
public class PageResult<T>{

    /**
     * 总条目数
     */
    private Long total;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 分页查询出的数据
     */
    private List<T> items;

    public PageResult(){ }

    public PageResult(Long total, List<T> list){
        this.total = total;
        this.items = list;
    }

    public PageResult(Long total, Integer totalPage, List<T> list){
        this.total = total;
        this.totalPage = totalPage;
        this.items = list;
    }
}
