package com.storehouse.com.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storehouse.com.entity.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreProductDto {
	private Long storeproductId;
	private StoreDto store;
	private ProductDto product;
	private int quantity;
	
}
