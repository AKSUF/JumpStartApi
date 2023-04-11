package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.StoreRequest;

public interface StoreRequestRepository extends JpaRepository<StoreRequest,Long> {

}
