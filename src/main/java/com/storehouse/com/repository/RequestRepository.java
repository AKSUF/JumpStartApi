package com.storehouse.com.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.User;

public interface RequestRepository extends JpaRepository<Request,Long> {


	List<Request> findRequestByProductProductId(Long productId);

	List<Request> findByProducer(User user);

	List<Request> findByStoreManager(User user);


	List<Request> findByStatusAndStoreManager(String name, User user);

	Request findProductByRequestId(Long productId);

	Request save(Product productrequest);






}
