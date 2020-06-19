package com.leyou.item.spu.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.brand.dao.BrandDao;
import com.leyou.item.brand.service.BrandService;
import com.leyou.item.category.dao.CategoryDao;
import com.leyou.item.category.service.CategoryService;
import com.leyou.item.domain.Brand;
import com.leyou.item.domain.Category;
import com.leyou.item.domain.Spu;
import com.leyou.item.enums.ExceptionEnum;
import com.leyou.item.exception.LeyouException;
import com.leyou.item.spu.dao.SpuDao;
import com.leyou.item.spu.rpo.GoodsRpo;
import com.leyou.item.spu.service.GoodsService;
import com.leyou.item.spu.vo.SpuVo;
import com.leyou.item.vo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 商品业务层接口实现类
 *
 * @author zc
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuDao spuDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    /**
     * 查询商品信息，完成的功能有：分页，搜索，条件过滤，商品更新时间排序
     * @param goodsRpo
     * @return
     */
    @Override
    public PageResult<SpuVo> pageQueryGoods(GoodsRpo goodsRpo) {
        // 1.分页
        PageHelper.startPage(goodsRpo.getPage(), goodsRpo.getRows());
        // 2.搜索字段过滤
        Example example = new Example(Spu.class);
        if (StringUtils.isNotEmpty(goodsRpo.getKey())) {
            example.createCriteria().andLike("title", "%"+goodsRpo.getKey()+"%");
        }
        // 3.上下架条件过滤,mysql默认boolean类型true就是1，false就是0
        if (goodsRpo.getSaleable() != null) {
            example.createCriteria().andEqualTo("saleable", goodsRpo.getSaleable());
        }
        // 4.默认根据商品更新时间排序
        String orderByClause = "last_update_time DESC";
        example.setOrderByClause(orderByClause);
        // 5.调用查询
        List<Spu> spus = spuDao.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)){
            log.error("商品查询失败，请求参数：{}",goodsRpo.toString());
            throw new LeyouException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        List<SpuVo> listSpuVo = getCnameAndBname(spus);
        PageInfo pageInfo = new PageInfo(spus);
        return new PageResult<SpuVo>(pageInfo.getTotal(), pageInfo.getPages(), listSpuVo);
    }

    /**
     * 根据cid和bid获取当前商品的分类名和品牌名，其中分类名要求是三级分类。
     * @param spus
     * @return
     */
    private List<SpuVo> getCnameAndBname(List<Spu> spus){
        // 用于存储每一个SpuVo对象
        List<SpuVo> spuvos = new ArrayList<>();
        SpuVo spuVo = null;
        for (Spu spu : spus){
            spuVo = new SpuVo();
            List<Long> list = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<Category> categorys = categoryService.getCategoryNameById(list);
            List<String> names = categorys.stream().map(Category::getName).collect(Collectors.toList());
            spuVo.setCname(StringUtils.join(names,"/"));
            Brand brand = brandService.getBrandById(spu.getBrandId());
            spuVo.setBname(brand.getName());
            spuVo.setId(spu.getId());
            spuVo.setTitle(spu.getTitle());
            spuvos.add(spuVo);
        }
        return spuvos;
    }
}
