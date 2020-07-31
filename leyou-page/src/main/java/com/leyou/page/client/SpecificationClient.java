package com.leyou.page.client;

import com.leyou.item.interf.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zc
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
