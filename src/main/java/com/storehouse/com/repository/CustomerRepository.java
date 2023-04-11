package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
