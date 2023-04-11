package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Producer;

public interface ProducerRepository extends JpaRepository<Producer,Long> {

}
