package com.leyou.item.category.controller;

import com.leyou.item.domain.Category;
import com.leyou.item.category.service.CategoryService;
import com.leyou.item.enums.ExceptionEnum;
import com.leyou.item.exception.LeyouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid, HttpServletRequest rq, HttpServletResponse response){
        if (pid == null){
            throw new LeyouException(ExceptionEnum.PID_CANNOT_BE_NULL);
        }
        List<Category> categories = categoryService.queryCategoryListByPid(pid);
        // return ResponseEntity.status(HttpStatus.OK).body(null); 传统写法
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
        return ResponseEntity.ok().build();
    }
}
