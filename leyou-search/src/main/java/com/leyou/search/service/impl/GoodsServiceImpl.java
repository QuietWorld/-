package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.interf.domain.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.domain.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zc
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private GoodsRepository repository;


    /**
     * 根据给定的Spu对象构建Goods对象并返回，在ES库中，一个Spu对应了一条数据（也就是一个Goods对象），
     *
     * @param spu spu对象，包含了多个sku
     * @return
     */
    @Override
    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        // 根据Spu的三级分类id，获取三级分类对象
        List<Category> categories = categoryClient.listCategoriesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new LeyouException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 获取分类名称
        List<String> categoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());

        // 获取品牌
        Brand brand = brandClient.getBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LeyouException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        // all：用来进行全文检索的字段，包括标题，分类，品牌
        // 拼接标题，分类名称，品牌名称
        String all = spu.getTitle() + StringUtils.join(categoryNames, " ") + brand.getName();

        // 获取Sku
        List<Sku> skus = goodsClient.listSkusBySpuId(spuId);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LeyouException(ExceptionEnum.SKU_NOT_FOUND);
        }
        // sku对象属性用作页面展示，所以需要去掉不需要的属性字段，且sku需要转换成json存入索引库，
        // 由于实体类不由我们维护所以我们不能使用@JsonIgnore来忽略某个字段后转换json
        // Map和普通的对象转换成json后的数据格式是一样的，所以定义一个Map集合来存储我们需要的Sku属性和属性值,一个Map就相当于一个Sku对象。
        List<Map<String, Object>> skuList = new ArrayList<>();
        Map<String, Object> skuMap = null;
        List<Long> priceList = new ArrayList<>();
        for (Sku sku : skus) {
            skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skuList.add(skuMap);
            priceList.add(sku.getPrice());
        }

        // 获取当前分类下所有可用于搜索的规格参数
        List<SpecParam> specParams = specClient.listSpecParams(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new LeyouException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        // 获取规格参数名
        List<String> specParamNames = specParams.stream().map(SpecParam::getName).collect(Collectors.toList());
        // 根据SpuId去SpuDetail表中获取规格参数的值
        SpuDetail spuDetail = goodsClient.getSpuDetailBySpuId(spuId);
        if (spuDetail == null) {
            throw new LeyouException(ExceptionEnum.SPUDETAIL_NOT_FOUND);
        }
        //
        // 获取Spu的通用规格参数,并将json格式的字符串转换成Map集合，key：规格参数id，value：规格参数值。
        Map<String, String> genericSpecMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
        // 获取Spu的特有规格参数，key：规格参数id，value：规格参数值。
        Map<String, List<String>> specialSpecMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec()
                , new TypeReference<Map<String, List<String>>>() {
                });
        // key:可搜索的规格参数名，value：规格参数值。
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam specParam : specParams) {
            String specParamName = specParam.getName();
            Object specParamValue = null;
            if (specParam.getGeneric()) {
                // 通用规格参数
                specParamValue = genericSpecMap.get(specParam.getId().toString());
                // 如果通用规格参数是数值类型，做分段优化
                if (specParam.getNumeric()) {
                    specParamValue = chooseSegment(specParamValue.toString(), specParam);
                }
            } else {
                // 特有规格参数
                specParamValue = specialSpecMap.get(specParam.getId().toString());
                // 特有规格参数是集合类型不是字符串，所以不进行分段优化。
            }
            specs.put(specParamName, specParamValue);
        }
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spuId);
        goods.setSubTitle(spu.getTitle());
        goods.setPrice(priceList);
        goods.setAll(all);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);
        return goods;
    }

    /**
     * 新增或者修改商品
     * 下面的三个调用都有可能出现异常，但是我们不必处理，调用insertOrUpdate方法的监听器
     * 方法一旦抛出异常，那么SpringAmqp就不会做ack确认，会触发重试。而一旦对三个调用进行了
     * try。即使出现异常但是由于我们将异常都try掉了，所以SpringAmqp会认为消息被成功的消费了，
     * 然后发送ack，队列中的消息消失，从而导致消息丢失。
     * @param spuId
     */
    @Override
    public void insertOrUpdateIndex(Long spuId) {
        // 根据spuId查询spu
        Spu spu = goodsClient.getSpuById(spuId);
        // 构建索引库所需goods对象
        Goods goods = this.buildGoods(spu);
        // 将goods保存到索引库
        repository.save(goods);
    }

    /**
     * 删除索引库中指定id的商品
     * @param spuId
     */
    @Override
    public void deleteIndex(Long spuId) {
        repository.deleteById(spuId);
    }

    /**
     * 如果规格参数是数值类型的，我们需要对其分段。
     * 例如：有一个规格参数是屏幕大小，这个规格参数需要用作搜索，却规格参数的值是数值类型的。参考传统电商网站发现，
     * 屏幕大小在作为搜索条件进行全文检索的时候是一个分段，就像这样（4.2寸-4.8寸）。但是我们索引库中，每个Spu保存的
     * Sku的屏幕尺寸都是一个具体的数值，就像（小米8商品5.5寸，小米2s商品4.2寸）。在这样的情况下，当用户点击(4.2寸-4.8寸)这个
     * 条件进行搜索的时候，我们应该怎么办，用range范围查询吗？貌似可以，将4.2寸和4.8寸作为最小值和最大值去索引库中根据屏幕尺寸
     * 进行range范围查询，就能得到结果了，这样看貌似可以没问题。但是想想这种情况，如果用户首先点击了查询5000价格以上的手机，那么
     * 此时展示在页面上的屏幕尺寸就应该变了，就像（用户不点击查询5000价格以上手机时屏幕尺寸有三个分段，分别是4.2-4.8,4.9-5.5,
     * 5.5及以上，但是用户点击了查询5000价格以上手机后，分段也应该对应的变化，因为索引库中可能根本没有5000价格以上而分段是
     * 4.2-4.8这个范围的手机了，如果再将4.2-4.8这个段展示出来显然不合适。），那么我们应该怎么办分段的信息才能根据索引库的商品实际
     * 情况进行展示？所以我们就需要将数值类型的规格参数进行分段了，就比如说从数据库中查出小米2s的屏幕大小的规格参数值是4.2寸，我们往
     * 索引库中存是就不再直接寸4.2寸了，而是存入一个范围，就存入4.2-4.8，也就是说从原先的key=屏幕大小，value=4.2
     * 变成了key=屏幕大小,value=4.2-4.8这样，然后页面进行展示的时候，我们先查询出5000价格以上的所有商品，然后去根据屏幕大小
     * 这个字段进行分段聚合，将聚合的结果展示在页面上，这样就解决了动态展示分段的问题。完美！！！哈哈哈！！！
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}