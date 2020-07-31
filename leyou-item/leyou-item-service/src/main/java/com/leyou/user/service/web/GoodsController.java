package com.leyou.user.service.web;


import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Sku;
import com.leyou.item.interf.domain.Spu;
import com.leyou.item.interf.domain.SpuDetail;
import com.leyou.item.interf.rpo.GoodsPageRpo;
import com.leyou.item.interf.vo.SpuVo;
import com.leyou.item.service.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 *
 * Resuful风格要求：
 *  1.不同的行为对应不同的请求方式（查询@GetMapping，新增@PostMapping，更新@PutMapping，删除@DeleteMapping）
 *  2.响应必须明确响应状态码
 *  ResponseEntity<Item> 该对象可以封装响应头 响应体 响应行等数据信息，响应体是什么泛型就应该写什么类型
 * 而@ResponseBoby只是将返回的对象序列化后作为响应体进行响应
 * 保存成功： return ResponseEntity.status(HttpStatus.CREATED).build
 * 更新成功： return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
 * 删除成功： return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
 * 查询成功   return ResponseEntity.ok(T body);
 *
 * @author zc
 */
@RestController
@RequestMapping
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
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新商品
     * @param spu
     * @return
     */
    @PutMapping("/goods")
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
     * 查询所有Spu
     * @return
     */
    @GetMapping("/spu/all")
    public ResponseEntity<List<Spu>> listAllSpus(){
        return ResponseEntity.ok(goodsService.listAllSpus());
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

    /**
     * 根据id查询Spu
     * @param id Spu的主键id
     * @return
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.getSpuById(id));
    }


    /**
     * 根据多个sku的id查询sku集合
     * @param ids
     * @return
     */
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> listSkusByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(goodsService.listSkusByIds(ids));
    }
}
