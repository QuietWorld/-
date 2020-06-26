package com.leyou.search.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 索引库的映射类
 * @author zc
 */
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {

    @Id
    private Long id;
    /**
     * 所有需要被搜索的信息，包含标题，分类，甚至品牌
     * 只有该字段是可分词的，用户的输入的所有查询都在该字段分词后的词汇列表中查询
     * 将需要被搜索的信息集中到一个字段可以提高搜索效率，比如用户输入手机，如果我们不做集中处理的话，我们可能去
     * 去title字段和subTitle字段查询，多字段查询效果显然不如在一个字段进行查询。
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;
    /**
     * 卖点，不分词不索引只展示
     */
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    /**
     * 品牌ID，查询商品规格参数的时候需要使用到
     */
    private Long brandId;
    /**
     * cid1 cid2 cid3都是作为搜索过滤条件
     */
    private Long cid1;
    private Long cid2;
    private Long cid3;
    /**
     * 创建时间，可以以字段进行商品的新品排序
     */
    private Date createTime;
    /**
     * 价格，索引库每条记录的单位是spu，而一个spu显然有多个sku，每个sku的price不一样，所以使用List集合来保存该spu下所有sku的price
     * 价格不分词被索引，可以用来做排序和范围过滤
     */
    private List<Long> price;
    /**
     * 同价格一样，一个spu下保存多个sku，sku是对象，可以使用一个List<Sku>来保存多个sku的信息，而且ES的字段是支持对象类型的，
     * 但是这里为了方便查询我们将List<Sku>转换成了json数据字符串，然后保存到ES的字段中
     */
    @Field(type = FieldType.Keyword, index = false)
    private String skus;
    /**
     * 可搜索的规格参数，key是参数名，值是参数值
     * 每个spu都有不同的规格参数名个规格参数值，显然我们不能将每种规格参数都作为一个字段，因为商品太多而规格参数都不同，就导致了规格参数字段
     * 无穷无尽，所以我们只定义一个specs字段来保存每个商品可以搜索的规格参数，将来将Map集合装换成json字符串保存到ES的索引库中
     * map里面可以保存很多个参数名和参数值的键值对，由于Map的value是Object类型，所以支持存储任意类型的数据，包括数组，
     * 到时候转换成json就像这样：{"specs":{"机身内存":["4GB","6GB"],"操作系统":"IOS"}}
     */
    private Map<String, Object> specs;

}
