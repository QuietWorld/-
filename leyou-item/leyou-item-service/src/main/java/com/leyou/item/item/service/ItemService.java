package com.leyou.item.item.service;

import com.leyou.item.item.domain.Item;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class ItemService {
    /**
     * 测试保存方法，返回保存后的用户
     * @param item
     * @return
     */
    public Item saveItem(Item item){
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
