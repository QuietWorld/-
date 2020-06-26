package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zc
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
