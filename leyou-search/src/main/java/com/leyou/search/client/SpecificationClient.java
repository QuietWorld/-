package com.leyou.search.client;

import com.leyou.item.domain.SpecGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("item-service")
public interface SpecificationClient {

    @GetMapping("/spec/groups/{cid}")
    List<SpecGroup> listSpecGroups(@PathVariable("cid")Long cid);
}
