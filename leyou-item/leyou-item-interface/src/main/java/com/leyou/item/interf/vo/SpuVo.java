package com.leyou.item.interf.vo;

import lombok.Data;

/**
 * 商品视图模型数据实体类
 * @author zc
 */
@Data
public class SpuVo {

    /**
     * spu id
     */
    private Long id;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品所属分类
     * */
    private String cname;
    /**
     * 商品所属品牌
     */
    private String bname;
}
