package com.storehouse.com.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CartDto {
	private long cartId;
	private List<CartItemDto>cartItems;
	@JsonIgnore
	private CustomerDto customer;
	@JsonIgnore
	private UserDto user;
	
}
