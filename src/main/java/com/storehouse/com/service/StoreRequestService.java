package com.storehouse.com.service;

import com.storehouse.com.dto.StoreRequestDto;
import com.storehouse.com.entity.StoreRequest;

public interface StoreRequestService {

	StoreRequest sendRequest(StoreRequestDto storeRequestDto, String token, Long productId);

}
