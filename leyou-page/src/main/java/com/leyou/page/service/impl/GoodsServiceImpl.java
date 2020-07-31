package com.leyou.page.service.impl;

import com.leyou.item.interf.domain.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import com.leyou.page.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author zc
 */


@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 根据spuId制作前台商品详情页模型数据
     *
     * @param id spu的id
     * @return
     */
    @Override
    public Map<String, Object> createModel(Long id) {
        // 查询Spu
        Spu spu = goodsClient.getSpuById(id);
        // 获取SpuDetail
        SpuDetail detail = spu.getSpuDetail();
        // 查询SKU集合
        List<Sku> skus = spu.getSkus();
        // 查询商品分类
        List<Category> categoryList = categoryClient.listCategoriesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 过滤商品分类无用字段，只留id和name
        List<Map<String, String>> categories = new ArrayList<>();
        Map<String, String> categoryMap = null;
        for (Category category : categoryList) {
            categoryMap = new HashMap<>();
            categoryMap.put("id", category.getId().toString());
            categoryMap.put("name", category.getName());
            categories.add(categoryMap);
        }
        // 查询品牌
        Brand brand = brandClient.getBrandById(spu.getBrandId());
        // 查询规格组及组内参数
        List<SpecGroup> specs = specClient.listSpecGroupsByCid(spu.getCid3());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("spu", spu);
        map.put("detail", detail);
        map.put("skus", skus);
        map.put("brand", brand);
        map.put("specs", specs);
        map.put("categories", categories);
        return map;
    }

    /**
     * 创建或修改静态页
     *
     * @param id spuId
     */
    @Override
    public void createHtml(Long id) {
        Context contex = new Context();
        Map<String, Object> model = this.createModel(id);
        contex.setVariables(model);
        File file = new File("D:\\temp", id + ".html");
        file.deleteOnExit();
        // jdk1.8的try...catch将创建对象当作try方法的局部参数，方法执行完成自动释放资源。
        try (PrintWriter writer = new PrintWriter(file)) {
            templateEngine.process("item", contex, writer);
        } catch (Exception e) {
            log.error("[静态化]页面静态化失败！", e);
        }
    }

    /**
     * 删除静态页
     *
     * @param spuId
     */
    @Override
    public void deleteHtml(Long spuId) {
        File file = new File("D:\\temp", spuId + ".html");
        file.deleteOnExit();
    }
}
