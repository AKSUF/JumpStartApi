package com.storehouse.com.dto;

import java.util.List;

import com.storehouse.com.entity.StoreRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StoreManagerDto {
	private Long storemanagerId;
	private UserDto userstoremanager;
	private StoreDto storemanager;
	private List<StoreRequestDto>storeRequests;
}
