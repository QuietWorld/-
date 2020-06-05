package com.leyou.brand.controller;

import com.github.pagehelper.PageInfo;
import com.leyou.brand.service.BrandService;
import com.leyou.brand.rpo.PageParam;
import com.leyou.category.domain.Brand;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LeyouException;
import com.leyou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
