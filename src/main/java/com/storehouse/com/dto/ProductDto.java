package com.storehouse.com.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import com.storehouse.com.entity.CartItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDto {
	private Long productId;
	private String productName;
	private Double price;
	private String image;
	

	private String category;
	  private Integer quantity;

	private UserDto producer;
	
	private Set<CommentDto> comments = new HashSet<>();
	
	
}
