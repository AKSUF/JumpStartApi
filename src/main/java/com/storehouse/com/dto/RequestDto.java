package com.storehouse.com.dto;

import java.util.List;
import java.util.Set;

import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
	
	private Long requestId;
    private ProductDto product;
    private int quantity;
	private Double totalPrice;
	private String paymentIntentId;
private String description;
    private UserDto producer;
    private UserDto storeManager;
    private String status;
    
 
    
}
