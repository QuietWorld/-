package com.leyou.item.service.service;

import com.leyou.item.interf.domain.SpecGroup;
import com.leyou.item.interf.domain.SpecParam;

import java.util.List;

/**
 * 规格组接口
 * @author zc
 */
public interface SpecificationService {

    /**
     * 根据商品分类id查询商品所有的规格组信息
     * @param cid 商品分类id
     * @return
     */
    List<SpecGroup> listSpecGroupsByCid(Long cid);

    /**
     * 查询规格参数集合
     *
     * @param gid 规格组id
     * @param cid 分类id
     * @param searching 该参数是否用于搜索过滤
     * @return
     */
    List<SpecParam> listSpecParams(Long gid, Long cid, Boolean searching);

    /**
     * 保存规格组
     * @param specGroup
     */
    void saveSpecGroup(SpecGroup specGroup);

    /**
     * 更新规格组
     * @param specGroup
     */
    void updateSpecGroup(SpecGroup specGroup);

    /**
     * 删除指定id的规格组
     * @param id 要删除的规格组的id
     */
    void deleteSpecGroupById(Long id);

    /**
     * 保存组类参数
     * @param specParam
     */
    void saveSpecParam(SpecParam specParam);

    /**
     * 更新组内参数
     * @param specParam
     */
    void updateSpecParam(SpecParam specParam);

    /**
     * 根据id删除规格参数
     * @param id 要删除的规格参数的id
     */
    void deleteSpecParamById(Long id);
}
