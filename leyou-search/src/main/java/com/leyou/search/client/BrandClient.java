package com.leyou.search.client;

import com.leyou.item.interf.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zc
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
