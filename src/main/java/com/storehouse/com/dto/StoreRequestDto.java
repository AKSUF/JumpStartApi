package com.storehouse.com.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.StoreManager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StoreRequestDto {
	private Long requestId;
	private ProductDto product;
	@JsonIgnore
	private StoreManagerDto storeManager;

	private boolean approved;
	
}
