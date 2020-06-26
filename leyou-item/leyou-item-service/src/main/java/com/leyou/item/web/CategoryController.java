package com.leyou.item.web;


import com.leyou.item.domain.Category;
import com.leyou.item.enums.ExceptionEnum;
import com.leyou.item.exception.LeyouException;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
}
