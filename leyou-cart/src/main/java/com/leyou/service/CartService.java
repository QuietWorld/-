package com.leyou.service;

import com.leyou.pojo.Cart;

import java.util.List;

/**
 * @author zc
 */
public interface CartService {

    /**
     * 添加购物车
     * @param cart
     */
    void saveCart(Cart cart);

    /**
     * 查询当前登录用户的购物车
     * @return
     */
    List<Cart> listCarts();

    /**
     * 根据当前登录用户的id和sku的id删除购物车商品,可以一次删除多个，所以传入id的集合进行批量删除
     * @param ids
     */
    void deleteCartsByIds(List<Long> ids);

    /**
     * 修改购物车单个商品数量
     * @param skuId
     * @param num
     */
    void updateCartNum(Long skuId, Integer num );
}
