package com.storehouse.com.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.StoreRequestDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.StoreManager;
import com.storehouse.com.entity.StoreRequest;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.StoreRequestRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.StoreRequestService;
import com.storehouse.com.service.StoresService;
@Service
public class StoreRequestServiceImpl implements StoreRequestService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private StoreRequestRepository storeRequestRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductRepository productRepository;
	@Override
	public StoreRequest sendRequest(StoreRequestDto storeRequestDto, String token,Long productId) {
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credential",email));
	Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","ProductId", productId.toString()));
		
		 StoreRequest storeRequest =this.modelMapper.map(storeRequestDto,StoreRequest.class);
		StoreManager storeManager=modelMapper.map(storeRequestDto.getStoreManager(),StoreManager.class);
		storeRequest.setStoreManager(storeManager);
		storeRequest.setProduct(product);
		
		return storeRequest;
	}

}
