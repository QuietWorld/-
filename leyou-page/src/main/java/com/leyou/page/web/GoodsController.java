package com.leyou.page.web;

import com.leyou.page.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

/**
 * 前台页面商品服务控制器
 * 前台页面的商品信息展示采用服务器端渲染的方式。
 * 后台的数据展示都是采用的异步发送ajax请求。
 * 前者和后者对比的区别是：
 * 服务器端渲染：点击跳转是直接请求到服务器端，服务器查询数据库，制作模型，转发到视图，在视图中根据模型对视图进行渲染。
 *      最终将渲染好的视图返回给客户端。
 *      渲染：从模型生成图像的过程
 * 异步ajax请求：点击跳转后，页面马上展示，页面向后台发起ajax请求，接收到后台响应的数据后才展示数据。
 * 服务端渲染古老的方式是采用的jsp，而SpringBoot弃用了jsp采用了模板引擎技术Thymeleaf。
 *
 * @author zc
 */


@Controller
@Slf4j
public class GoodsController {

   @Autowired
   private GoodsService goodsService;



    /**
     * 页面展示商品详情
     * 这里要查询的数据：
     * - SPU
     * - SpuDetail
     * - SKU集合
     * - 商品分类
     *   - 这里值需要分类的id和name就够了，因此我们查询到以后自己需要封装数据
     * - 品牌
     * - 规格组
     *   - 查询规格组的时候，把规格组下所有的参数也一并查出，上面提供的接口中已经实现该功能，我们直接调
     * @param id SpuId
     * @return
     */
    @GetMapping("{id}.html")
    public ModelAndView toItemPage(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView();
        Map<String, Object> modelMap = goodsService.createModel(id);
        mv.addAllObjects(modelMap);
        mv.setViewName("item");
        return mv;
    }
}
