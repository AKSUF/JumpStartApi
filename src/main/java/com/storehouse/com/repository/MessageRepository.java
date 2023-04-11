package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Message;

public interface MessageRepository extends JpaRepository<Message,Long>{

}
