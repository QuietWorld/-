package com.leyou.web;

import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zc
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveCart(@RequestBody Cart cart){
        cartService.saveCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询当前登录用户的购物车
     * 该方法没有参数，我们根据当前登录用户的id在redis中进行查询，tomcat为每个请求分配一个单独的线程，
     * 用户的信息保存在ThreadLocal线程域中
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Cart>> listCarts(){
        List<Cart> carts = cartService.listCarts();
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车单个商品数量
     * @param skuId
     * @param num
     */
    @PutMapping
    public ResponseEntity<Void> updateCartNum(@RequestParam("id") Long skuId, @RequestParam("num") Integer num){
        cartService.updateCartNum(skuId, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据当前登录用户的id和sku的id删除购物车商品,可以一次删除多个，所以传入id的集合进行批量删除
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteCartsByIds(@RequestParam("ids") List<Long> ids){
        cartService.deleteCartsByIds(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
