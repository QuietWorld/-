package com.leyou.item.spu.controller;

import com.leyou.item.domain.Spu;
import com.leyou.item.spu.rpo.GoodsRpo;
import com.leyou.item.spu.service.GoodsService;
import com.leyou.item.spu.vo.SpuVo;
import com.leyou.item.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 完成对商品Spu的各个模块调用查询
 * @author zc
 */
@RestController
@RequestMapping("/spu")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询商品信息
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<SpuVo>> pageQueryGoods(GoodsRpo goodsRpo){
        if (goodsRpo.getRows() == null){
            goodsRpo.setRows(5);
        }
        if (goodsRpo.getPage() == null){
            goodsRpo.setPage(1);
        }
        PageResult<SpuVo> pageResult = goodsService.pageQueryGoods(goodsRpo);
        return ResponseEntity.ok(pageResult);
    }
}
