package com.leyou.item.service.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.item.interf.domain.SpecGroup;
import com.leyou.item.interf.domain.SpecParam;
import com.leyou.item.service.dao.SpecGroupDao;
import com.leyou.item.service.dao.SpecParamDao;
import com.leyou.item.service.service.SpecificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 商品规格参数组业务层
 *
 * @author zc
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupDao specGroupDao;

    @Autowired
    private SpecParamDao specParamDao;

    private static final Logger log = LoggerFactory.getLogger(SpecificationServiceImpl.class);


    /**
     * 根据商品id查询该商品所有的规格组信息
     * @param cid 商品分类id
     * @return
     */
    @Override
    public List<SpecGroup> listSpecGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specGroupDao.select(specGroup);
        if (CollectionUtils.isEmpty(specGroups)) {
            log.info("没有查询到商品分类ID为{}的规格组信息", specGroup.getCid());
            throw new LeyouException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }else {
            log.info("[specGroups]:{}", specGroups);
        }
        return specGroups;
    }

    /**
     * 保存规格组
     *
     * @param specGroup
     */
    @Transactional(rollbackFor = {Throwable.class})
    @Override
    public void saveSpecGroup(SpecGroup specGroup) {
        log.info(specGroup.toString());
        int insert = specGroupDao.insert(specGroup);
        if (insert != 1){
            log.error("规格组保存失败");
            throw new LeyouException(ExceptionEnum.SPEC_GROUP_SAVE_ERROR);
        }
    }

    /**
     * 查询参数集合
     *
     * @param gid 规格组id
     * @param cid 分类id
     * @param searching 该参数是否用于搜索过滤
     * @return
     */
    @Override
    public List<SpecParam> listSpecParams(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        // 通用Mapper会根据specParam对象的非空字段作为条件进行查询
        List<SpecParam> specParams = specParamDao.select(specParam);
        if (CollectionUtils.isEmpty(specParams)){
            throw new LeyouException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return specParams;
    }

    /**
     * 更新规格组
     *
     * @param specGroup
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void updateSpecGroup(SpecGroup specGroup) {
        log.info("更新前：{}", specGroup.toString());
        int updates = specGroupDao.updateByPrimaryKey(specGroup);
        if (updates == 0){
            log.info("规格组未进行任何更新");
        }else if(updates != 1){
            log.info("规格组更新失败");
            throw new LeyouException(ExceptionEnum.SPEC_GROUP_UPDATE_ERROR);
        }
    }

    /**
     * 删除指定id的规格组
     * @param id 要删除的规格组的id
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteSpecGroupById(Long id) {
        int deletes = specGroupDao.deleteByPrimaryKey(id);
        if (deletes != 1){
            log.error("规格组删除失败");
            throw new LeyouException(ExceptionEnum.SPEC_GROUP_DELETE_ERROR);
        }
    }

    /**
     * 保存组类参数
     * @param specParam
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveSpecParam(SpecParam specParam) {
        log.info("保存组类参数：",specParam.toString());
        int insert = specParamDao.insert(specParam);
        if (insert != 1){
            log.error("组类参数保存失败");
            throw new LeyouException(ExceptionEnum.SPEC_PARAM_SAVE_ERROR);
        }
    }

    /**
     * 更新组内参数
     * @param specParam
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateSpecParam(SpecParam specParam) {
        log.info("update:{}",specParam.toString());
        int update = specParamDao.updateByPrimaryKey(specParam);
        if (update == 0){
            log.warn("规格组内参数未进行任何更新");
        }else if (update != 1){
            log.error("规格组内参数更新失败");
            throw new LeyouException(ExceptionEnum.SPEC_PARAM_UPDATE_ERROR);
        }
    }

    /**
     * 删除指定id的规格组内参数
     *
     * @param id 要删除的规格组内参数的id
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteSpecParamById(Long id) {
        log.info("delete spec param, id is {}", id);
        int delete = specParamDao.deleteByPrimaryKey(id);
        if (delete != 1){
            log.error("规格组内参数删除失败");
            throw new LeyouException(ExceptionEnum.SPEC_PARAM_DELETE_ERROR);
        }
    }


}
