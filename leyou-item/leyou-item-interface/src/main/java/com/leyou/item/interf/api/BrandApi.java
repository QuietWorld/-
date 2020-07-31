package com.leyou.item.interf.api;

import com.leyou.item.interf.domain.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zc
 */
@RequestMapping("/brand")
public interface BrandApi {

    /**
     * 根据品牌id查询品牌
     * @param id 品牌id
     * @return
     */
    @GetMapping("/{id}")
    Brand getBrandById(@PathVariable("id") Long id);

    /**
     * 根据品牌id集合批量查询品牌
     * @param ids
     * @return
     */
    @GetMapping("/list")
    List<Brand> listBrandsByIds(@RequestParam("ids") List<Long> ids);
}
