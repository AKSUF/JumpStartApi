package com.storehouse.com.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.storehouse.com.dto.UserDto;
import com.storehouse.com.entity.User;

public interface UserService {
	// uploading user profile image
		void uploadImage(MultipartFile multipartFile, String token) throws IOException;

		// user create profile
		UserDto createUserProfile(UserDto userDto, String token);
		
		User getUser(Long userId);
		
		List<UserDto> getUserProfiles();
		
		//edit user profile 
		UserDto editUserProfile(UserDto userDto, Long userId);

		List<User> getUserByRole(String role, String token);

		
}
