package com.storehouse.com.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CustomerDto {
	private Long customerId;
	private List<StoreDto>stores;
	private CartDto cart;
}
