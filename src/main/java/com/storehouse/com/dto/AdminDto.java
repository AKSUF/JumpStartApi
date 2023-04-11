package com.storehouse.com.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class AdminDto {

	private Long adminId;
	private UserDto adminuser;
	private List<StoreDto>stores;
	
}
