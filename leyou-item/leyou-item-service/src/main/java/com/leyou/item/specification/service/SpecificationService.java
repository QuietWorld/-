package com.leyou.item.specification.service;

import com.leyou.item.domain.SpecGroup;
import com.leyou.item.domain.SpecParam;

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
    List<SpecGroup> listSpecGroups(Long cid);

    /**
     * 根据规格组id查询组类参数
     * @param gid 规格组id
     * @return
     */
    List<SpecParam> listSpecParams(Long gid);

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
    void deleteSpecGroup(Long id);

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
     * 删除指定id的规格组内参数
     * @param id 要删除的规格组内参数的id
     */
    void deleteSpecParam(Long id);
}
