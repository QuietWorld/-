package com.leyou.item.specification.controller;

import com.leyou.item.domain.SpecGroup;
import com.leyou.item.domain.SpecParam;
import com.leyou.item.specification.service.SpecificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规格组控制器
 *
 * @author zc
 */
@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;
    private static final Logger log = LoggerFactory.getLogger(SpecificationController.class);

    /**
     * 根据商品分类id查询该分类下的规格组信息
     * @param cid 商品分类id
     * @return
     */
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> listSpecGroups(@PathVariable("cid") Long cid) {
        if (cid == null){
            return ResponseEntity.badRequest().build();
        }
        List<SpecGroup> specGroups = specificationService.listSpecGroups(cid);
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 根据规格组id查询该组下的规格参数信息
     * @param gid 规格组id
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> listSpecParams(@RequestParam("gid") Long gid){
        if (gid == null){
            return ResponseEntity.badRequest().build();
        }
        List<SpecParam> specParams = specificationService.listSpecParams(gid);
        return ResponseEntity.ok(specParams);
    }

    /**
     * 保存规格组
     * @param specGroup
     * @return
     */
    @PostMapping("/group")
    public ResponseEntity<Void> saveSpecGroup(SpecGroup specGroup){
        log.info("save:{}", specGroup.toString());
        specificationService.saveSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新规格组
     * @param specGroup
     * @return
     */
    @PutMapping("/group")
    public ResponseEntity<Void> updateSpecGroup(SpecGroup specGroup){
        log.info("update:{}", specGroup.toString());
        specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除指定id的规格组
     * @param id 规格组的id
     * @return
     */
    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id){
        log.info("delete:{}", id);
        if (id == null){
            return ResponseEntity.badRequest().build();
        }
        specificationService.deleteSpecGroup(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 保存规格组内参数
     * @param specParam
     * @return
     */
    @PostMapping("/param")
    public ResponseEntity<Void> saveSpecParam(SpecParam specParam){
        specificationService.saveSpecParam(specParam);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新规格组内参数
     * @param specParam
     * @return
     */
    @PutMapping("/param")
    public ResponseEntity<Void> updateSpecParam(SpecParam specParam){
        specificationService.updateSpecParam(specParam);
        return ResponseEntity.ok().build();
    }

    /**
     *  删除指定id的规格组内参数
     * @param id 规格组内参数的id
     * @return
     */
    @DeleteMapping("/param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id")Long id){
        specificationService.deleteSpecParam(id);
        return ResponseEntity.ok().build();
    }
}
