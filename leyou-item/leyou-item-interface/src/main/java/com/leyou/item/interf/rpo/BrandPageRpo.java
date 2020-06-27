package com.leyou.item.interf.rpo;

import lombok.Data;

/**
 *  request param object
 *  封装分页查询请求参数的类
 * @author zc
 */

@Data
public class BrandPageRpo {

    /**
     * 搜索关键字
     */
    private String key;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 每页查询的条数
     */
    private Integer rows;
    /**
     * 通过什么进行排序
     */
    private String sortBy;
    /**
     * 排序方式
     */
    private Boolean desc;


    public Integer getPage() {
        if (page == null){
            page = 1;
        }
        return page;
    }

    public Integer getRows() {
        if (rows == null){
            rows = 5;
        }
        return rows;
    }


}
