package com.storehouse.com.service;

import java.util.List;

import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Delivery;

public interface DeliverService {

	List<DeliveryDto> getAllOrderlist(String token);

	List<DeliveryDto> getAllOrderForDeliveryman(String token);

	Delivery getDeliveryRequestById(Long deliveryId);

	boolean acceptRequest(Long deliveryId, String token);

	List<DeliveryDto> getAllAccepted(Long customerId, String token);

}
