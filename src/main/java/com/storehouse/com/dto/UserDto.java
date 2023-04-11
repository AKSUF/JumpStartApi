package com.storehouse.com.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {

	private Long user_id;
	@NotEmpty(message="Name can not be empty")
	@Size(max = 100, min = 3, message = "Name must be between 3 to 100 characters")
	private String name;
	
	private String detail;
	private String phone_number;
	private String gender;
	private String profile_image;
	private String adress;
	
}
