package com.leyou.search.vo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Brand;
import com.leyou.item.interf.domain.Category;
import com.leyou.search.domain.Goods;

import java.util.List;
import java.util.Map;

/**
 * 封装全文检索查询结果的实体类
 * note:在java中一般情况下，注解是不能继承的。
 *
 * @author zc
 */
public class SearchResult extends PageResult<Goods> {

    /**
     * 品牌集合
     */
    List<Brand> brands;
    /**
     * 分类集合
     */
    List<Category> categories;

    /**
     * 规格参数集合，每个Map代表一个规格参数和规格参数的待选项
     */
    List<Map<String, Object>> specs;

    public SearchResult() {
    }

    /**
     * 子类构造中必须调用父类的构造方法，不写则赠送默认的super()方法
     * super关键字可以看作是父类的引用。
     *
     * @param total
     * @param totalPage
     * @param goodsList
     * @param brands
     * @param categories
     */
    public SearchResult(Long total, Integer totalPage, List<Goods> goodsList, List<Brand> brands
            , List<Category> categories) {
        super(total, totalPage, goodsList);
        this.brands = brands;
        this.categories = categories;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> list
            , List<Brand> brands, List<Category> categories, List<Map<String, Object>> specs) {
        super(total, totalPage, list);
        this.brands = brands;
        this.categories = categories;
        this.specs = specs;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "brands=" + brands +
                ", categories=" + categories +
                ", specs=" + specs +
                '}';
    }

}
