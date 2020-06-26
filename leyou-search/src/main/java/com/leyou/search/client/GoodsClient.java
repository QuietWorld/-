package com.leyou.search.client;

import com.leyou.item.domain.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author
 */

@FeignClient("item-service")
public interface GoodsClient {


    /**
     * 根据spuId查询该spu下的所有sku
     *
     * @param spuId
     * @return
     */
    @GetMapping("/goods/sku/list")
    List<Sku> listSkusBySpuId(@RequestParam("id") Long spuId);

}
