package com.storehouse.com.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct,Long> {

	Collection<? extends StoreProduct> findByStore(Store store);




}
