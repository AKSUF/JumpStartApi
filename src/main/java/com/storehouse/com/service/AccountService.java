package com.storehouse.com.service;

import java.util.List;

import com.storehouse.com.dto.AccountDto;
import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.User;

public interface AccountService {
	// get all accounts
	List<AccountDto> getAllAccountDto();

	// get particular account info
	AccountDto getAccountDto(String token);

	Account getAccount(String token);
	
	void deleteAccount(Long userId);

	
}
