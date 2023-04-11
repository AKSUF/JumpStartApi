package com.storehouse.com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemDto {
	private Long orderitemId;
	private ProductDto product;
	private OrdersDto order;
}
