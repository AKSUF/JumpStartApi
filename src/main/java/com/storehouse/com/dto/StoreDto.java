package com.storehouse.com.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StoreDto {
	private Long storeId;
	private String storeName;
	private String storeLocation;
	private String catagory;
	private String openday;
	private String storeImage;
	private List<CustomerDto>customers;
	private List<DeliveryManDto>deliveryMans;
	private UserDto storeManager;
	private AdminDto admin;
	private Set<CommentDto> hicomments = new HashSet<>();
}
