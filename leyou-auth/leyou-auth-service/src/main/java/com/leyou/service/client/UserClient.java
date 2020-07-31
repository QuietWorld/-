package com.leyou.service.client;

import com.leyou.user.interf.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zc
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
