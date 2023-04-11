package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long> {

}
