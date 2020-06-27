package com.leyou.item.service.web;


import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Brand;
import com.leyou.item.interf.rpo.BrandPageRpo;
import com.leyou.item.service.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<PageResult<Brand>> pageQueryBrands(BrandPageRpo rpb){
        return ResponseEntity.ok(brandService.pageQueryBrands(rpb));
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新品牌，包括对品牌基本信息，品牌的商品分类信息和品牌logo的更新。
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        if (brand == null || CollectionUtils.isEmpty(cids)){
            return ResponseEntity.badRequest().build();
        }
        brandService.updateBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据分类id查询该分类下所有品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> listBrandsByCid(@PathVariable("cid") Long cid){
        if (cid == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(brandService.listBrandsByCid(cid));
    }

    /**
     * 根据品牌id查询品牌
     * @param id 品牌id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable("id") Long id){
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }
}
