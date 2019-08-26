package com.m520it.service.impl;

import com.m520it.dao.ItemDao;
import com.m520it.domain.Item;
import com.m520it.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Override
    @Transactional
    public void save(Item item) {
        itemDao.save(item);
    }

    @Override
    public List<Item> findAll(Item item) {
        //创建查询的条件
        Example<Item> example=Example.of(item);
        //根据条件进行查询,并且返回查询的结果
        List<Item> list = itemDao.findAll(example);
        return list;
    }
}
