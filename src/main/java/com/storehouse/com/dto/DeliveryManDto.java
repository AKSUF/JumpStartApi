package com.storehouse.com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryManDto {
	private Long deliverymanId;
	private UserDto userdelivryman;
	private StoreDto store;
	private DeliveryDto delivery;
}
