package com.storehouse.com.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Delivery;
import com.storehouse.com.entity.User;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

	List<Delivery> findByUser(User user);

	List<Delivery> findByStatus(String delivery_status);

	Collection<DeliveryDto> findByStatusAndCustomer(String name, User user);

}
