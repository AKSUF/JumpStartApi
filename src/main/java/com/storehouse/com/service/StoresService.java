package com.storehouse.com.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.storehouse.com.dto.StoreDto;
import com.storehouse.com.dto.StoreProductDto;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.StoreProduct;

public interface StoresService {

	StoreDto createStore(@Valid StoreDto storeDto, String jwtFromRequest);

	

	void deleteStore(Long storeId, String token);

	List<StoreDto> getAllCatagory(String token);

	StoreDto getStore(Long storeId, String token);

	StoreDto getStoreById(Long storeId);

	StoreDto updateImage(StoreDto storeDto, String jwtFromRequest, Long storeId);

	StoreProduct sendPaymentProduct(StoreProductDto storeProductDto, Long productId, Long storeId,
			String token,Long requestId) throws Exception;



	StoreDto updateStore(@Valid StoreDto storeDto, Long storeId, HttpServletRequest request);



	List<StoreProductDto> getStoreProduct(String jwtFromRequest);



	List<StoreProductDto> getStoreProductasUser(String token);

	

	

	
}
