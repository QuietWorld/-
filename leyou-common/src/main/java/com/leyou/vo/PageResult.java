package com.leyou.vo;

import lombok.Data;

import java.util.List;

/**
 *  view object
 * 封装分页查询后数据的类
 * @author zc
 */
@Data
public class PageResult<T>{

    private Long total;
    private Integer totalPage;
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
