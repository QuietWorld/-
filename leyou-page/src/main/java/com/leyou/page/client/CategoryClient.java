package com.leyou.page.client;

import com.leyou.item.interf.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zc
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
