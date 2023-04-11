package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Sales;

public interface SalesRepository extends JpaRepository<Sales,Long> {

}
