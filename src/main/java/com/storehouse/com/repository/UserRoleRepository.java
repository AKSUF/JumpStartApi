package com.storehouse.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

}
