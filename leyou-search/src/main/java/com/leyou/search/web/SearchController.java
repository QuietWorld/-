package com.leyou.search.web;


import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.rpo.SearchPageRpo;
import com.leyou.search.domain.Goods;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台页面搜索控制器
 * @author  zc
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 前台搜索框根据搜索条件进行分页查询
     *
     * @param searchPageRpo
     * @return 分页查询一般都包含两个属性，当前页数据和总条数信息
     */
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> pageQueryGoods(@RequestBody SearchPageRpo searchPageRpo){
        return ResponseEntity.ok(searchService.pageQueryGoods(searchPageRpo));
    }
}
