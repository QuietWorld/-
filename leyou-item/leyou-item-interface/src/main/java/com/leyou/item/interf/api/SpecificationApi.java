package com.leyou.item.interf.api;

import com.leyou.item.interf.domain.SpecGroup;
import com.leyou.item.interf.domain.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zc
 */
public interface SpecificationApi {

    /**
     * 查询规格参数集合
     *
     * @param gid 规格组id
     * @param cid 分类id
     * @param searching 该参数是否用于搜索过滤
     * @return
     */
    @GetMapping("/spec/params")
    List<SpecParam> listSpecParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching);

    /**
     * 根据三级分类id查询规格参数组信息
     * @param cid 分类id
     * @return
     */
    @GetMapping("/groups/{cid}")
    List<SpecGroup> listSpecGroupsByCid(@PathVariable("cid") Long cid);
}
