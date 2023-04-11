package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
	

}
