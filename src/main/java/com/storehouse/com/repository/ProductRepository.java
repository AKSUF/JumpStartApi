package com.storehouse.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.User;

public interface ProductRepository extends JpaRepository<Product,Long> {

	List<Product> findByUser(User user);







}
