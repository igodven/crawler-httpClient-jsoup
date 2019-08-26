package com.m520it.service;

import com.m520it.domain.Item;

import java.util.List;

public interface ItemService {

    /**
     * 保存商品的信息
     * @param item
     */
    void save(Item item);

    /**
     * 查询出商品的信息
     *       *如果数据库中拥有这种商品,就无须保存
     */
    List<Item> findAll(Item item);
}
