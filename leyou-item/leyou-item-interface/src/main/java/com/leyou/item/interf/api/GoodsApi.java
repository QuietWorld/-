package com.leyou.item.interf.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Sku;
import com.leyou.item.interf.domain.Spu;
import com.leyou.item.interf.domain.SpuDetail;
import com.leyou.item.interf.rpo.GoodsPageRpo;
import com.leyou.item.interf.vo.SpuVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *  远程调用客户端接口应该由服务的提供方提供而不是服务的调用方，因为服务的调用方不可能知道API的路径，
 *  而且就算知道API的路径，一旦开发方变化路径，那么调用方也没法知道，所以由开发方提供远程调用客户端。
 *  所以我们在leyou-item-interface这个模块新增一个api包，里面存放远程调用客户端接口。调用方只需要
 *  导入这个模块的依赖就能使用接口，在客户端我们通常定义一个接口来继承开发方提供的远程调用客户端接口，
 *  并在定义的接口上面使用@FeignClient来声明为远程调用接口，实现远程的调用。
 *
 * @author zc
 */
public interface GoodsApi {

    /**
     * 根据spuId查询该spu下的所有sku
     *
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    List<Sku> listSkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 查询所有Spu
     * @return
     */
    @GetMapping("/spu/all")
    List<Spu> listAllSpus();

    /**
     * 根据SpuId查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    SpuDetail getSpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据id查询Spu
     * @param id Spu的主键id
     * @return
     */
    @GetMapping("/spu/{id}")
    Spu getSpuById(@PathVariable("id") Long id);
}
