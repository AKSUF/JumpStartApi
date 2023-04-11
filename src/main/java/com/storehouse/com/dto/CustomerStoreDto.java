package com.storehouse.com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CustomerStoreDto {
	private Long customerstoreId;
	private CustomerDto customer;
	private StoreDto store; 
}
