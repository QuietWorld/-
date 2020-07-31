package com.leyou.user.service.web;


import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.item.interf.domain.Category;
import com.leyou.item.service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  商品分类的控制器
 * @author zc
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点id查询商品分类
     * @param pid 父节点Id
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> listCategoriesByPid(@RequestParam("pid") Long pid){
        if (pid == null){
            throw new LeyouException(ExceptionEnum.PID_CANNOT_BE_NULL);
        }
        List<Category> categories = categoryService.listCategoriesByPid(pid);
        return ResponseEntity.ok(categories);
    }

    /**
     *  保存分类信息
     * @param category
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Void> saveCategory(Category category){
        if (category == null){
            return ResponseEntity.badRequest().build();
        }
        categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     *  根据商品分类id集合查询商品分类集合
     * @param ids 多个分类id
     * @return
     */
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> listCategoriesByIds(@RequestParam("ids") List<Long> ids){
        List<Category> categories = categoryService.listCategoriesByIds(ids);
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据分类id查询分类
     * @param id 分类id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
