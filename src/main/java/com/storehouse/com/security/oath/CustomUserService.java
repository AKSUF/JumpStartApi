package com.storehouse.com.security.oath;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.storehouse.com.entity.Account;
import com.storehouse.com.repository.AccountRepository;

@Service
public class CustomUserService implements UserDetailsService {
	@Autowired
	private AccountRepository accountRepo;
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
		return UserPrincipal.create(account);
	}
}
