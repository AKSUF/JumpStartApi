package com.storehouse.com.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storehouse.com.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DeliveryDto {
	private Long deliverId;
	private Integer deliveryNumber;
	private String status;
	private String address;
	private ProductDto product;
	private UserDto customer;
	private OrdersDto orderdeliver;
	private Double totalPrice;
	private int quantity;
	private String paymentIntentId;
	
}
