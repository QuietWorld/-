package com.leyou.item.brand.controller;

import com.leyou.item.brand.service.BrandService;
import com.leyou.item.brand.rpo.PageParam;
import com.leyou.item.domain.Brand;
import com.leyou.item.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author zc
 */

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param rpb
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> pageQuery(PageParam rpb){
        return ResponseEntity.ok(brandService.pageQuery(rpb));
    }

    /**
     *  保存品牌
     * @param brand
     * @param cids  品牌的商品的分类id
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        if (brand == null || CollectionUtils.isEmpty(cids)){
            return ResponseEntity.badRequest().build();
        }
        brandService.saveBrand(brand, cids);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新品牌，包括对品牌基本信息，品牌的商品分类信息和品牌logo的更新。
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        System.out.println("执行！！！");
        if (brand == null || CollectionUtils.isEmpty(cids)){
            return ResponseEntity.badRequest().build();
        }
        brandService.updateBrand(brand, cids);
        return ResponseEntity.ok().build();
    }
}
