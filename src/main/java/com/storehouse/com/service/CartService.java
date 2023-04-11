package com.storehouse.com.service;

import java.util.List;

import com.storehouse.com.dto.CartDto;
import com.storehouse.com.dto.CartItemDto;

public interface CartService {



	CartDto addCart(Long productId, String token, CartItemDto cartItemDto);

	CartDto getCart(Long cartId, String token);

	CartDto updateCartItem(Long cartItemId, String token);

	void removeCartItem(Long cartItemId, String token);

	CartDto getUserCart(String token);


	List<CartDto> getCartByUser(String token);

	void updateCartItemQuantity(Long cartitemId, int quantity, String token);

	

	



	
}
