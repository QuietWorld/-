package com.leyou.search.service.impl;

import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Brand;
import com.leyou.item.interf.domain.Category;
import com.leyou.item.interf.domain.SpecParam;
import com.leyou.item.interf.rpo.SearchPageRpo;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.domain.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 前台查询服务接口实现类
 *
 * @author zc
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specClient;

    /**
     * 根据关键字进行分页查询
     *
     * @param searchPageRpo
     * @return
     */
    @Override
    public PageResult<Goods> pageQueryGoods(SearchPageRpo searchPageRpo) {
        // 创建本地搜索查询构建器对象
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        // 分页
        searchQueryBuilder.withPageable(PageRequest.of(searchPageRpo.getPage() - 1, searchPageRpo.getRows()));
        // 根据关键字去all字段中使用match查询
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("all", searchPageRpo.getKey());
        searchQueryBuilder.withQuery(queryBuilder);
        // 多余字段过滤
        searchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        // 排序
        if (StringUtils.isNotBlank(searchPageRpo.getOrderBy())) {
            searchQueryBuilder.withSort(SortBuilders.fieldSort(searchPageRpo.getOrderBy()).order(searchPageRpo.getOrder() ? SortOrder.ASC : SortOrder.DESC));
        }
        // 品牌，分类，规格参数过滤(页面传递的是品牌的id和三级分类的id)
        Map<String, String> filterMap = searchPageRpo.getFilter();
        // 根据分类聚合（其实就是将查询出的所有Spu按照cid3进行分组）
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("categoryAgg").field("cid3"));
        // 根据品牌聚合
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brandId"));
        // 查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(searchQueryBuilder.build());
        // 解析品牌和分类聚合结果
        LongTerms categoryAgg = (LongTerms) goodsPage.getAggregation("categoryAgg");
        LongTerms brandAgg = (LongTerms) goodsPage.getAggregation("brandAgg");
        List<Category> categories = this.parseCategoryAgg(categoryAgg);
        List<Brand> brands = this.parseBrandAgg(brandAgg);
        // 如果按照分类进行聚合后，得到的结果是搜索结果中只有一个分组(也就是只有一个分类)，那么就进行规格参数的聚合
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            specs = this.aggSpecification(categories.get(0).getId(),queryBuilder);
        }
        // 解析分页结果
        int totalPages = goodsPage.getTotalPages();
        long total = goodsPage.getTotalElements();
        List<Goods> goodsList = goodsPage.getContent();
        return new SearchResult(total, totalPages, goodsList, brands, categories, specs);
    }

    /**
     *  1.在明确了搜索结果只有一个分类时才进行规格参数的聚合，需要根据单个三级分类id查询出分类下所有可用于搜索的规格参数
     *  2.规格参数的聚合一定是在用户已经搜索出结果之后进行聚合。想想这个场景，就是用户搜索的是小米手机，如果在用户搜索之前
     *      进行聚合就会发生聚合出来的内存选项中是258G，而小米手机根本没有258G这个选项规格的内存，显然很不合理。
     *
     * @param cid 三级分类id
     * @param queryBuilder 前置搜索条件
     * @return
     */
    private List<Map<String, Object>> aggSpecification(Long cid, QueryBuilder queryBuilder){
        List<Map<String, Object>> specs = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        List<SpecParam> specParams = null;
        try {
            // 根据cid3查询出当前分类下所有可搜索的规格参数
            specParams = this.specClient.listSpecParams(null, cid, true);
        } catch (Exception e) {
            log.error("[搜索服务]规格参数参数失败 参数信息：分类id：{} 异常信息：{}", cid, e);
            return null;
        }
        List<String> specParamNames = specParams.stream().map(SpecParam::getName).collect(Collectors.toList());
        // 根据规格参数名进行聚合
        for (String specParamName : specParamNames) {
            searchQueryBuilder.addAggregation(AggregationBuilders
                    .terms(specParamName).field("specs." + specParamName + ".keyword"));
        }
        searchQueryBuilder.withQuery(queryBuilder);
        AggregatedPage<Goods> aggregatedPage = (AggregatedPage<Goods>) goodsRepository.search(searchQueryBuilder.build());
        for (String specParamName : specParamNames){
            StringTerms stringTerms = (StringTerms) aggregatedPage.getAggregation(specParamName);
            List<String> specParamValues = this.parseSpecValueAgg(stringTerms);
            // 装入Map
            Map<String, Object> specsMap = new HashMap<>();
            specsMap.put("k", specParamName);
            specsMap.put("options", specParamValues);
            specs.add(specsMap);
        }
        return specs;
    }

    /**
     * 解析规格参数值聚合结果
     *
     * @param stringTerms
     * @return
     */
    private List<String> parseSpecValueAgg(StringTerms stringTerms){
        List<String> specParamValues = stringTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
        return specParamValues;
    }

    /**
     * 解析分类聚合结果
     *
     * @param longTerms
     * @return
     */
    private List<Category> parseCategoryAgg(LongTerms longTerms) {
        try {
            List<Long> ids = longTerms.getBuckets().stream()
                    .map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
            // 可能查询不出分类，那么就会抛出new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
            // 为了使程序能够正常的运行，对异常进行捕获
            List<Category> categories = categoryClient.listCategoriesByIds(ids);
            return categories;
        } catch (Exception e) {
            log.error("[搜索服务]分类查询失败", e);
            return null;
        }
    }

    /**
     * 解析品牌聚合结果
     *
     * @param longTerms
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms longTerms) {
        try {
            List<Long> brandIds = longTerms.getBuckets().stream().
                    map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
            // 可能查询不出品牌，那么就会抛出new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
            // 为了使程序能够正常的运行，对异常进行捕获
            List<Brand> brands = brandClient.listBrandsByIds(brandIds);
            return brands;
        } catch (Exception e) {
            log.error("[搜索服务]品牌查询失败", e);
            return null;
        }
    }

}
