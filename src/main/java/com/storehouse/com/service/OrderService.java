package com.storehouse.com.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.storehouse.com.dto.DeliveryAssignmentDto;
import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Delivery;
import com.storehouse.com.entity.DeliveryAssignment;
import com.storehouse.com.entity.User;

public interface OrderService {

	Delivery orderProduct(@Valid DeliveryDto deliveryDto, Long productId, String jwtFromRequest);

	DeliveryDto orderStatus(User user, Long deliveryId, String status);

	List<DeliveryDto> userOrders(String jwtFromRequest);

	DeliveryDto confirmOrderdelivery(Long deliveryId, String token);

	List<DeliveryDto> allOrders();

	DeliveryDto singleorder(Long deliveryId);

	List<DeliveryDto> pendingOrders();

	List<DeliveryDto> getAllOrders(String token);

	Delivery newOrder(@Valid DeliveryDto deliveryDto, Long productId, String token);

	DeliveryAssignment assgindeliveryman(DeliveryAssignmentDto deliveryAssignmentDto, String token, Long deliveryId,
			Long deliverymanId);


	//List<DeliveryDto> delivermanConfirmorders(String token);





	//List<DeliveryDto> delivermanConfirmorders(String token,String searchquery);

	//List<DeliveryDto> delivermanConfirmorders(String token);

}
