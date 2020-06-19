package com.leyou.item.spu.rpo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 *  封装分页查询spu的请求参数的实体类
 * @author zc
 */
@Data
public class GoodsRpo {

    /**
     * 搜索关键字
     */
    private String key;

    /**
     * true--上架
     * false--下架
     */
    private Boolean saleable;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页显示的条数
     */
    private Integer rows;

}
