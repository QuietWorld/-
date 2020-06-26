package com.leyou.item.web;

import com.leyou.item.domain.Sku;
import com.leyou.item.domain.Spu;
import com.leyou.item.domain.SpuDetail;
import com.leyou.item.rpo.GoodsPageRpo;
import com.leyou.item.service.GoodsService;
import com.leyou.item.vo.PageResult;
import com.leyou.item.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 保存成功： return ResponseEntity.status(HttpStatus.CREATED).build
 * 更新成功： return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
 * 删除成功： return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
 * 查询成功   return ResponseEntity.ok(T body);
 *
 * @author zc
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 保存商品
     * 保存商品包括了对数据库三张表的操作，tb_spu表，tb_spuDetail表,tb_sku表
     * tb_spu和tb_spuDetail本质上是同一种表进行了垂直拆分，都是记录了同一类sku的通用
     * @param spu
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新商品
     * @param spu
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        // 更新和删除成功响应204,也就是HttpStatus.NO_CONTENT
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 分页查询商品信息
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuVo>> pageQueryGoods(GoodsPageRpo rpo){
        if (rpo.getRows() == null){
            rpo.setRows(5);
        }
        if (rpo.getPage() == null){
            rpo.setPage(1);
        }
        PageResult<SpuVo> pageResult = goodsService.pageQueryGoods(rpo);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 根据SpuId查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        if (spuId == null){
            return ResponseEntity.notFound().build();
        }
        SpuDetail spuDetail = goodsService.getSpuDetailBySpuId(spuId);
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spuId查询该spuId下的所有Sku
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> listSkusBySpuId(@RequestParam("id") Long spuId){
        return ResponseEntity.ok(goodsService.listSkusBySpuId(spuId));
    }
}
