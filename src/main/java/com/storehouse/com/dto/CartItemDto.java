package com.storehouse.com.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CartItemDto {
	private Long cartitemId;

	private ProductDto product;
	@JsonIgnore
	private CartDto cart;
	private int quantity;
}
