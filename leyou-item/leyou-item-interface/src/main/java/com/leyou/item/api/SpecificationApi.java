package com.leyou.item.api;

import com.leyou.item.domain.SpecGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author zc
 */
public interface SpecificationApi {

    /**
     * 根据分类id查询规格参数组
     *
     * @param cid 分类id
     * @return
     */
    @GetMapping("/spec/groups/{cid}")
    List<SpecGroup> listSpecGroups(@PathVariable("cid")Long cid);
}
