package com.leyou.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LeyouException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.interceptor.UserInterceptor;
import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zc
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid:";

    /**
     * 添加购物车
     *
     * 用户的购物车保存到redis中，使用redis的hash结构进行存储，
     * redis的hash结构对应java是双层Map，就是这样：Map<String,Map<String,String>>
     * 外层Map的key是用户的id，value是用户购物车数据
     * 内层Map的key是购物车商品的id，value是商品的一些参数信息
     * note：在redis中，保存的所有数据都是字符串，即使是hash结构，key是map的json格式
     *
     * @param cart
     */
    @Override
    public void saveCart(Cart cart) {
        BoundHashOperations<String, Object, Object> operations = getOperations();
        // 记录新增的数量
        Integer num = cart.getNum();
        String hashKey = cart.getSkuId().toString();
        // 判断当前商品是否在当前登录用户的购物车中存在
        if (operations.hasKey(hashKey)) {
            // 是，增加数量
            // 在内存map中根据skuid获取到商品信息，redis中保存的数据是字符串，商品信息保存进去后转换成了json，所以取出来也是json
            String value = operations.get(hashKey).toString();
            // json转Cart对象
            cart = JsonUtils.parse(value, Cart.class);
            // 增加数量
            cart.setNum(num + cart.getNum());
        }
        // 添加商品
        operations.put(hashKey, JsonUtils.serialize(cart));
    }

    /**
     * 查询当前登录用户的购物车
     *
     * @return
     */
    @Override
    public List<Cart> listCarts() {
        BoundHashOperations<String, Object, Object> operations = getOperations();
        List<Object> values = operations.values();
        List<Cart> carts = values.stream()
                // 将Object转换成String后通过JsonUtils解析为Cart对象
                .map(o -> JsonUtils.parse(o.toString(), Cart.class))
                .collect(Collectors.toList());
        return carts;
    }

    /**
     * 根据当前登录用户的id和sku的id删除购物车商品,可以一次删除多个，所以传入id的集合进行批量删除
     *
     * @param ids
     */
    @Override
    public void deleteCartsByIds(List<Long> ids) {
        BoundHashOperations<String, Object, Object> operations = getOperations();
        Long deleteCount = operations.delete(ids);
        if (deleteCount < 1) {
            throw new LeyouException(ExceptionEnum.CART_DELETE_ERROR);
        }
    }

    /**
     * 修改购物车单个商品数量
     *
     * @param skuId
     * @param num
     */
    @Override
    public void updateCartNum(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> operations = getOperations();
        String hashKey = skuId.toString();
        String value = operations.get(hashKey).toString();
        // json转Cart对象
        Cart cacheCart = JsonUtils.parse(value, Cart.class);
        // 增加数量
        cacheCart.setNum(num);
        operations.put(hashKey, JsonUtils.serialize(cacheCart));
    }

    private BoundHashOperations<String, Object, Object> getOperations() {
        // 从线程域中获取当前登录用户
        Long loginUserId = UserInterceptor.getLoginUser().getId();
        // 从redis中取出数据
        String key = KEY_PREFIX + loginUserId;
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        return operations;
    }
}
