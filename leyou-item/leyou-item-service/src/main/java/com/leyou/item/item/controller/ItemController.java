package com.leyou.item.item.controller;

import com.leyou.item.item.domain.Item;
import com.leyou.item.enums.ExceptionEnum;
import com.leyou.item.exception.LeyouException;
import com.leyou.item.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 测试通用异常处理的方法，与项目本身无关，为了复习所以保留。
     * Resuful风格要求：
     * 1.新增方法必须是post请求
     * 2.响应必须明确响应状态码
     * ResponseEntity<Item> 该对象可以封装响应头 响应体 响应行等数据信息，响应体是什么泛型就应该写什么类型
     * 而@ResponseBoby只是将返回的对象序列化后作为响应体进行响应
     * @param item
     * @return
     */
    @PostMapping
    public ResponseEntity<Item> save(Item item){
        if (item.getPrice() == null || (item.getPrice()+"").equalsIgnoreCase("null")){
            // 抛出自定义异常
            throw new LeyouException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
        }
        Item item1 = itemService.saveItem(item);
        return new ResponseEntity<Item>(item1, HttpStatus.CREATED);
    }
}
