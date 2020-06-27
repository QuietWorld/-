package com.leyou.item.service.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.enums.ExceptionEnum;
import com.leyou.item.exception.LeyouException;
import com.leyou.item.service.dao.SkuDao;
import com.leyou.item.service.dao.SpuDao;
import com.leyou.item.service.dao.SpuDetailDao;
import com.leyou.item.service.dao.StockDao;
import com.leyou.item.interf.rpo.GoodsPageRpo;
import com.leyou.item.service.service.BrandService;
import com.leyou.item.service.service.CategoryService;
import com.leyou.item.service.service.GoodsService;
import com.leyou.item.interf.vo.SpuVo;
import com.leyou.item.vo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    @Autowired
    private SpuDetailDao spuDetailDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private StockDao stockDao;
    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    /**
     * 查询商品信息，完成的功能有：分页，搜索，条件过滤，商品更新时间排序
     *
     * @param goodsRpo
     * @return
     */
    @Override
    public PageResult<SpuVo> pageQueryGoods(GoodsPageRpo goodsRpo) {
        // 1.分页
        PageHelper.startPage(goodsRpo.getPage(), goodsRpo.getRows());
        // 2.搜索字段过滤
        Example example = new Example(Spu.class);
        if (StringUtils.isNotEmpty(goodsRpo.getKey())) {
            example.createCriteria().andLike("title", "%" + goodsRpo.getKey() + "%");
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
        if (CollectionUtils.isEmpty(spus)) {
            log.error("商品查询失败，请求参数：{}", goodsRpo.toString());
            throw new LeyouException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        List<SpuVo> listSpuVo = getCnameAndBname(spus);
        PageInfo pageInfo = new PageInfo(spus);
        return new PageResult<SpuVo>(pageInfo.getTotal(), pageInfo.getPages(), listSpuVo);
    }

    /**
     * 保存商品
     *
     * @param spu
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveGoods(Spu spu) {
        log.info("开始保存：{}", spu.toString());
        // 1.保存商品spu信息
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        // 默认上架
        spu.setSaleable(true);
        // 不用于逻辑删除
        spu.setValid(false);
        int rowCount = spuDao.insert(spu);
        if (rowCount != 1) {
            log.error("spu保存失败,失败的spu：{}", spu.toString());
            throw new LeyouException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

        // 2.保存商品spuDatail信息
        SpuDetail spuDetail = spu.getSpuDetail();
        // 这里需要注意的是：保存spuDetail时需要指定spuId，而这个spuId是上面保存spu后回显的spuId，所以保存spuDetail一定是在保存spu之后
        spuDetail.setSpuId(spu.getId());
        rowCount = spuDetailDao.insert(spuDetail);
        if (rowCount != 1) {
            log.error("spuDetail保存失败,失败的spuDetail：{}", spuDetail.toString());
            throw new LeyouException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

        // 3.保存商品sku和stock信息
        this.saveSkuAndStock(spu);

    }

    /**
     * 根据spuId查询SpuDetail
     *
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail getSpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailDao.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            log.error("spuDetail查询失败，spuId：{}", spuId);
            throw new LeyouException(ExceptionEnum.SPUDETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
     * 查询该spuId下的所有Sku
     *
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> listSkusBySpuId(Long spuId) {
        if (spuId == null) {
            log.error("sku查询失败,spuId为null");
            throw new LeyouException(ExceptionEnum.SKU_NOT_FOUND);
        }
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skuList = skuDao.select(record);
        if (CollectionUtils.isEmpty(skuList)) {
            log.error("sku查询失败");
            throw new LeyouException(ExceptionEnum.SKU_NOT_FOUND);
        }
        // 查询库存
       /* for (Sku sku : skuList){
            Stock stock = stockDao.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        }*/
        skuList.forEach(sku -> {
            Stock stock = stockDao.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skuList;
    }

    /**
     * 商品更新
     * 由于商品更新涉及到了对sku的更新，用户可能会对sku进行删除，更新或者新增，我们在后台
     * 判断用户对那些sku进行了删除或者更新或者新增会比较麻烦（但是可以实现，在下面我自己写的
     * 商品更新就实现了），所以我们对于sku更新实现是这么做的：先根据spuId删除所有的sku和库存，
     * 再将页面传过来的sku全部进行新增
     * <p>
     * 通常情况下：
     * 新增：先新增主表，因为从表需要使用主表的主键
     * 删除：先删除从表，因为删除主表时如果有从表约束会导致删除失败
     *
     * @param spu
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateGoods(Spu spu) {
        // 1.删除stock和sku
        // 1-1.根据spuId查询要删除的skuId
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> deleteSkuList = skuDao.select(sku);
        if (CollectionUtils.isEmpty(deleteSkuList)) {
            log.error("查询sku失败");
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        List<Long> deleteSkuIds = deleteSkuList.stream().map(s -> s.getId()).collect(Collectors.toList());
        // 1-2.批量删除stock
        int deleteCount = stockDao.deleteByIdList(deleteSkuIds);
        if (deleteCount != deleteSkuIds.size()) {
            log.error("删除stock失败");
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        // 1-3根据spuId删除sku
        deleteCount = skuDao.delete(sku);
        if (deleteCount != deleteSkuIds.size()) {
            log.error("删除sku失败");
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        // 2.保存sku和stock
        this.saveSkuAndStock(spu);

        // 3.更新Spu
        // 以下属性不允许更新
        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        spu.setValid(null);
        spu.setSaleable(true);
        // updateByPrimaryKeySelective和updateByPrimaryKey的区别：
        // updateByPrimaryKeySelective：根据主键更新属性不为null的值
        // updateByPrimaryKey：根据主键更新实体全部字段，null值会被更新
        int rowCount = spuDao.updateByPrimaryKeySelective(spu);
        if (rowCount != 1) {
            log.error("商品更新失败,spu:{}", spu.toString());
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }

        // 4.更新SpuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        rowCount = spuDetailDao.updateByPrimaryKeySelective(spuDetail);
        if (rowCount != 1) {
            log.error("商品详情更新失败:{}", spuDetail.toString());
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
    }

    /**
     * 我写的商品更新，很麻烦！
     *
     * @param spu
     */
    @Transactional(rollbackFor = Throwable.class)
    public void myUpdateGoods(Spu spu) {
        // 更新Spu
        spu.setLastUpdateTime(new Date());
        int rowCount = spuDao.updateByPrimaryKey(spu);
        if (rowCount != 1) {
            log.error("商品更新失败,spu:{}", spu.toString());
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }

        // 更新SpuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        rowCount = spuDetailDao.updateByPrimaryKey(spuDetail);
        if (rowCount != 1) {
            log.error("商品详情更新失败:{}", spuDetail.toString());
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }

        // 更新sku
        // 根据spuId去sku表中查询该spuId下的所有sku
        Sku oldSku = new Sku();
        oldSku.setSpuId(spu.getId());
        List<Long> oldSkuIds = new ArrayList<>();
        skuDao.select(oldSku).forEach(sku -> {
            oldSkuIds.add(sku.getId());
        });
        List<Sku> skus = spu.getSkus();
        List<Long> newSkuIds = new ArrayList<>();
        skus.forEach(sku -> {
            newSkuIds.add(sku.getId());
        });
        List<Long> deleteSkuIds = new ArrayList<>();
        // 遍历原有的skuId集合，如果该原有的skuId不在新的skuId集合里面，那么就可以认为原有的skuId对应的sku被用户删除了
        for (Long oldSkuId : oldSkuIds) {
            if (!newSkuIds.contains(oldSkuId)) {
                deleteSkuIds.add(oldSkuId);
            }
        }
        // 根据deleteSkuIds批量删除sku和库存
        int deleteCount = skuDao.deleteByIdList(deleteSkuIds);
        if (deleteCount != deleteSkuIds.size()) {
            log.error("sku删除失败");
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        deleteCount = stockDao.deleteByIdList(deleteSkuIds);
        if (deleteCount != deleteSkuIds.size()) {
            log.error("sku删除失败");
            throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        for (Sku sku : skus) {
            // 如果不是新增的sku那么进行更新
            if (sku.getId() != null && oldSkuIds.contains(sku.getId())) {
                sku.setLastUpdateTime(new Date());
                rowCount = skuDao.updateByPrimaryKey(sku);
                if (rowCount != 1) {
                    log.error("sku更新失败,sku:{}", sku.toString());
                    throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
                }
            } else {
                // 如果是新增的sku那么进行保存
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(sku.getCreateTime());
                sku.setSpuId(spu.getId());
                rowCount = skuDao.insert(sku);
                if (rowCount != 1) {
                    log.error("sku保存失败,失败的sku：{}", sku.toString());
                    throw new LeyouException(ExceptionEnum.GOODS_SAVE_ERROR);
                }
            }

            // 更新stock
            Stock stock = new Stock();
            stock.setStock(sku.getStock());
            rowCount = stockDao.updateByPrimaryKey(stock);
            if (rowCount != 1) {
                log.error("库存量更新失败：stock:{}", stock.toString());
                throw new LeyouException(ExceptionEnum.GOODS_UPDATE_ERROR);
            }
        }
    }


    /**
     * 根据cid和bid获取当前商品的分类名和品牌名，其中分类名要求是三级分类。
     *
     * @param spus
     * @return
     */
    private List<SpuVo> getCnameAndBname(List<Spu> spus) {
        // 用于存储每一个SpuVo对象
        List<SpuVo> spuvos = new ArrayList<>();
        SpuVo spuVo = null;
        for (Spu spu : spus) {
            spuVo = new SpuVo();
            List<Long> list = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<Category> categorys = categoryService.listCategoriesByIds(list);
            List<String> names = categorys.stream().map(Category::getName).collect(Collectors.toList());
            spuVo.setCname(StringUtils.join(names, "/"));
            Brand brand = brandService.getBrandById(spu.getBrandId());
            spuVo.setBname(brand.getName());
            spuVo.setId(spu.getId());
            spuVo.setTitle(spu.getTitle());
            spuvos.add(spuVo);
        }
        return spuvos;
    }

    private void saveSkuAndStock(Spu spu) {
        // 定义一个库存对象集合
        List<Stock> stockList = new ArrayList<>();
        for (Sku sku : spu.getSkus()) {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getLastUpdateTime());
            int insertRowCount = skuDao.insert(sku);
            if (insertRowCount != 1) {
                log.error("sku保存失败,失败的sku：{}", sku.toString());
                throw new LeyouException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            Stock stock = new Stock();
            // 此skuId是上面sku新增成功后回显的id，所以新增sku一定是在新增stock之前
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }

        // 保存商品库存信息,适应通用Mapper的扩展接口InsertListMapper<T>接口中的insertList方法进行批量新增
        int count = stockDao.insertList(stockList);
        if (count != stockList.size()) {
            log.error("商品库存新增失败");
            throw new LeyouException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }
}
