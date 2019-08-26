package com.m520it.dao;

import com.m520it.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemDao extends JpaRepository<Item,Long>, JpaSpecificationExecutor<Item> {
}
