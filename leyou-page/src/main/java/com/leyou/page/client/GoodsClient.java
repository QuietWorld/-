package com.leyou.page.client;

import com.leyou.item.interf.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
