package com.storehouse.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storehouse.com.entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {
	public Optional<Account> findByEmail(String email);
}
