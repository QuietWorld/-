package com.leyou.item.service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.interf.domain.Brand;
import com.leyou.item.service.dao.BrandDao;
import com.leyou.item.interf.rpo.BrandPageRpo;
import com.leyou.item.service.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * 商品业务
 *
 * @author zc
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;
    private static final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);

    /**
     * 分页查询品牌
     *
     * @param rpb
     * @return
     */
    @Override
    public PageResult<Brand> pageQueryBrands(BrandPageRpo rpb) {
        // 分页
        PageHelper.startPage(rpb.getPage(), rpb.getRows());
        // 过滤  根据关键字搜索查询(模糊查询) select * from brand where name like "%xxx%" or letter = "xxx" order by id desc;
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(rpb.getKey())) {
            // 定义模糊查询的标准，由于数据库表中商品首字母都是大写，所以使用toUpperCase()方法。
            example.createCriteria().orLike("name", rpb.getKey()).orEqualTo("letter", rpb.getKey().toUpperCase());
        }
        // 排序   如果传递的sortBy参数为empty("")或null则不排序
        if (StringUtils.isNotBlank(rpb.getSortBy())) {
            // String orderByClause = "id desc";
            String orderByClause = rpb.getSortBy() + " " + (rpb.getDesc() ? "DESC" : "ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Brand> brands = brandDao.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LeyouException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        PageInfo pageInfo = new PageInfo(brands);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 保存品牌
     * 保存品牌的基本数据是在品牌表中插入数据，保存品牌的商品的分类信息实在品牌和分类的中间表中插入数据
     *
     * @param brand
     * @param cids
     */
    @Transactional(rollbackFor = {Error.class, RuntimeException.class})
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        // 保存品牌基本信息
        int updates = brandDao.insert(brand);
        if (updates != 1) {
            log.warn("品牌保存失败：{}", brand.toString());
            throw new LeyouException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        // 保存品牌分类信息
        for (Long cid : cids) {
            updates = brandDao.saveBrandCategory(brand.getId(), cid);
            if (updates != 1) {
                log.warn("品牌分类保存失败!品牌id：{},分类id:{}", brand.getId(), cid);
                throw new LeyouException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
        log.info("新增品牌信息：{},品牌的商品分类信息：{}", brand.toString(), cids.toString());
    }

    /**
     * 更新品牌，包括更新品牌的基本信息，品牌的商品的分类id，品牌的logo。品牌的logo更新在update-service微服务完成。
     * 当用户选择图片后，发送请求到upload-service微服务logo图片保存到专门的文件存储服务器。文件存储服务器返回图片的
     * 保存地址。
     *
     * @param brand
     * @param cids 品牌下商品所属的分类id
     */
    @Transactional(rollbackFor = {Error.class, RuntimeException.class})
    @Override
    public void updateBrand(Brand brand, List<Long> cids) {
        if (brand.getId() == null) {
            log.warn("商品id为空");
            throw new LeyouException(ExceptionEnum.BRANDID_CANNOT_BE_NULL);
        }
        // 更新品牌基本信息
        int updates = brandDao.updateByPrimaryKey(brand);
        if (updates != 1) {
            log.error("品牌基本信息更新失败:{}", brand.toString());
            throw new LeyouException(ExceptionEnum.BRAND_UPDATE_ERROR);
        }
        // 更新品牌商品的分类信息
        for (Long cid : cids) {
            brandDao.updateCategoryBrand(brand.getId(), cid);
        }
    }

    /**
     * 根据品牌id获取品牌
     *
     * @param id 品牌id
     * @return
     */
    @Override
    public Brand getBrandById(Long id) {
        Brand brand = brandDao.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LeyouException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * 根据分类id查询出该分类下所有品牌
     *
     * @param cid 分类id
     * @return
     */
    @Override
    public List<Brand> listBrandsByCid(Long cid) {
        List<Brand> brandList = brandDao.listBrandsByCid(cid);
        if (CollectionUtils.isEmpty(brandList)){
            log.info("【{}】分类编号下没有查询到品牌信息", cid);
            throw new LeyouException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brandList;
    }
}
