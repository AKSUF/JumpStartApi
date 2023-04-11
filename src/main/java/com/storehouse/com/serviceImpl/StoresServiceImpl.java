package com.storehouse.com.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.dto.StoreDto;
import com.storehouse.com.dto.StoreProductDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.StoreProduct;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.RequestRepository;
import com.storehouse.com.repository.StoreProductRepository;
import com.storehouse.com.repository.StoreRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.StoresService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Service
public class StoresServiceImpl implements StoresService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private StoreProductRepository storeProductRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private Environment environment;
	@Autowired
	 private UserRepository userRepository;
	
	@Override
	public StoreDto createStore(@Valid StoreDto storeDto, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		Store store = this.modelMapper.map(storeDto, Store.class);
//store.setUser(account.getUser());
		User user = account.getUser();
		store.setStoreManager(account.getUser());

		Store saveStore = this.storeRepository.save(store);
		return this.modelMapper.map(saveStore, StoreDto.class);
	}

	@Override
	public StoreDto updateStore(@Valid StoreDto storeDto, Long storeId, HttpServletRequest request) {
		Store store = this.storeRepository.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store", "Store Id", storeId.toString()));

		store.setCatagory(storeDto.getCatagory());
		store.setOpenday(storeDto.getOpenday());
	
		store.setStoreLocation(storeDto.getStoreLocation());
		Store updateStore = this.storeRepository.save(store);
		return this.modelMapper.map(updateStore, StoreDto.class);
	}

	@Override
	public void deleteStore(Long storeId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		Store store = this.storeRepository.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store", "Store Id", storeId.toString()));
		System.out.println(store);
		this.storeRepository.delete(store);
	}

	@Override
	public List<StoreDto> getAllCatagory(String token) {
		List<Store> stores = this.storeRepository.findAll();
		List<StoreDto> storeDtos = stores.stream().map((store) -> this.modelMapper.map(store, StoreDto.class))
				.collect(Collectors.toList());
		System.out.println(storeDtos);
		return storeDtos;
	}

	@Override
	public StoreDto getStore(Long storeId, String token) {
		Store store = this.storeRepository.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store", "Store Id", storeId.toString()));
		System.out.println(store);
		return this.modelMapper.map(store, StoreDto.class);
	}

	@Override
	public StoreDto getStoreById(Long storeId) {
		Store store = this.storeRepository.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store", "Store Id", storeId.toString()));
		return this.modelMapper.map(store, StoreDto.class);
	}

	@Override
	public StoreDto updateImage(StoreDto storeDto, String jwtFromRequest, Long storeId) {
		Store store = this.storeRepository.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store", "Storeid", storeId.toString()));
		String email = jwtUtils.getUserNameFromToken(jwtFromRequest);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credntial", email));
		store.setStoreImage(storeDto.getStoreImage());
		Store updateStore = this.storeRepository.save(store);
		return this.modelMapper.map(updateStore, StoreDto.class);
	}

	@Override
	public StoreProduct sendPaymentProduct(StoreProductDto storeProductDto, Long productId, Long storeId, String token,
	        Long requestId) throws Exception {

	    try {
	    	String stripeApiKey = environment.getProperty("stripe.api.key");
			Stripe.apiKey = stripeApiKey;
	        String email = jwtUtils.getUserNameFromToken(token);
	        Account account = accountRepository.findByEmail(email)
	                .orElseThrow(() -> new ResourceNotFoundException("user", "credntial", email));
	        Product product = productRepository.findById(productId)
	                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId.toString()));
	        Request request = requestRepository.findById(requestId)
	                .orElseThrow(() -> new ResourceNotFoundException("Request", "requestId", requestId.toString()));
	        System.out.println("");
	        Map<String, Object> params = new HashMap<>();
	        params.put("amount", Math.round(request.getTotalPrice() * 100));
	        params.put("currency", "BDT");
	        params.put("payment_method_types", Arrays.asList("card"));
	        PaymentIntent intent = PaymentIntent.create(params);
	        request.setPaymentIntentId(intent.getId());
	        request.setStoreManager(account.getUser());
	        request.setProduct(product);
	        
	        requestRepository.save(request);
	        Store store = storeRepository.findByStoreManager(account.getUser());
	        StoreProduct storeProduct = this.modelMapper.map(storeProductDto, StoreProduct.class);
	        storeProduct.setProduct(product);
	       
	        storeProduct.setStore(store);
	        storeProduct.setRequest(request);
	        return this.storeProductRepository.save(storeProduct);
	    } catch (StripeException e) {
	        // handle the exception here, e.g. log it or throw a different exception
	        throw new Exception("Error processing payment: " + e.getMessage());
	    }
	}

	@Override
	public List<StoreProductDto> getStoreProduct(String token) {
	List<StoreProduct>getAllStoreProduct=this.storeProductRepository.findAll();
	List<StoreProductDto>getAllStoreProductDto= getAllStoreProduct.stream().map((store) -> this.modelMapper.map(store, StoreProductDto.class))
			.collect(Collectors.toList());
		return getAllStoreProductDto;
	}

	@Override
	public List<StoreProductDto> getStoreProductasUser(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user=account.getUser();

		  List<Store>stores= storeRepository.findAllByStoreManager(user);
		List<StoreProduct>storeProducts=new ArrayList<StoreProduct>();
		for(Store store :stores) {
			storeProducts.addAll(storeProductRepository.findByStore(store));
			
		}
		List<Product>products=new ArrayList<>();
		for(StoreProduct storeProduct:storeProducts) {
			if(storeProduct.getProduct().isRequestSent()) {
				products.add(storeProduct.getProduct());
			}
		}
		
		List<StoreProduct>storeProducts2=this.storeProductRepository.findAll();
		List<StoreProductDto>storeProductDtos=storeProducts2.stream().map((store)->this.modelMapper.map(store,StoreProductDto.class)).collect(Collectors.toList());
		return storeProductDtos;
		
		
	   
	}

}
