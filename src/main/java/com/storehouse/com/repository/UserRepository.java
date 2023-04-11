package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {




}
