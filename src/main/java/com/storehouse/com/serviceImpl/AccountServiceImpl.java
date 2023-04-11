package com.storehouse.com.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.AccountDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.User;
import com.storehouse.com.entity.UserRole;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.repository.UserRoleRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtils jwtUtils;
	

	
	@Autowired
	private UserRoleRepository userRoleRepository;
	

	@Override
	public List<AccountDto> getAllAccountDto() {

		List<Account> allAccounts = this.accountRepo.findAll();

		List<AccountDto> allAccountDtos = allAccounts.stream()
				.map((account) -> this.modelMapper.map(account, AccountDto.class)).collect(Collectors.toList());

		return allAccountDtos;
	}

	@Override
	public AccountDto getAccountDto(String token) {

		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("account", "credentials", email));

		return this.modelMapper.map(account, AccountDto.class);
	}
	
	@Override
	public Account getAccount(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		return accountRepo.findByEmail(email).get();
	}
	
	@Override
	public void deleteAccount(Long userId) {
		User user = userRepo.findById(userId).get();
	
		Account account = user.getAccount();
		List<UserRole> userRoles = account.getUserRoles();
		if(userRoles != null) {
			userRoles.stream().forEach((role) -> {
				userRoleRepository.delete(role);
			});
		}
		userRepo.delete(user);
		accountRepo.delete(account);
		return;
	}
	
	
}
