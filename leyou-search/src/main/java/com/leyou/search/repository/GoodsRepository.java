package com.leyou.search.repository;

import com.leyou.search.domain.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zc
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
