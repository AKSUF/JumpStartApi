package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{

}
