package com.storehouse.com.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class OrdersDto {
	private Long orderId;
	private List<OrderItemDto> orderItems;
	private DeliveryDto delivry;
	private CustomerDto customer;
}
