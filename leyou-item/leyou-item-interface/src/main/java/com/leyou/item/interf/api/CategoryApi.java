package com.leyou.item.interf.api;

import com.leyou.item.interf.domain.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zc
 */
public interface CategoryApi {


    /**
     *  根据商品分类id集合查询商品分类集合
     * @param ids 多个分类id
     * @return
     */
    @GetMapping("/category/list/ids")
    List<Category> listCategoriesByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据分类id查询分类
     * @param id 分类id
     * @return
     */
    @GetMapping("/category/{id}")
    Category getCategoryById(@PathVariable("id") Long id);
}
