package com.storehouse.com.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.storehouse.com.dto.ApiResponse;
import com.storehouse.com.dto.UserDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.User;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.AccountService;
import com.storehouse.com.service.FileService;
import com.storehouse.com.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private ModelMapper modelMapper;
	@Value("${project.image}")
	private String path;
	

	// register local user
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUserLocal(HttpServletRequest request, @Valid @RequestBody UserDto userDto) {


		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.createUserProfile(userDto, jwtUtils.getJWTFromRequest(request)));
	}

	// register local user
	@PutMapping("/update/{userId}")
	public ResponseEntity<UserDto> editUserProfile(
			HttpServletRequest request, @Valid @RequestBody UserDto userDto,
			@PathVariable Long userId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.editUserProfile(userDto, userId));
	}
	
	
	
	
	
	
	// get personal user profile
	@GetMapping("/profile")
	public ResponseEntity<?> getPersonalProfile(HttpServletRequest request){
		System.out.println("/////////request");
		System.out.println(request);
		System.out.println("/////////request");
		String token =  jwtUtils.getJWTFromRequest(request);
		System.out.println("/////////request");
		Account account = accountService.getAccount(token);
		System.out.println(token+"TYhis is token");
		System.out.println("/////////request");
		User user = account.getUser();
		System.out.println(user+"TYhis is token");
		return ResponseEntity.ok(this.modelMapper.map(user, UserDto.class));
	}
	
	//delete user 
	@DeleteMapping("/{uid}")
	public ResponseEntity<?> deleteUser(@PathVariable Long uid){
		accountService.deleteAccount(uid);
		return ResponseEntity.ok("Account has been deleted successfully");
	}

	// uploading user profile image
	@PostMapping("/image/upload-profile-image")
	public ResponseEntity<ApiResponse> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile image)
			throws IOException {

		// insuring the request has a file
		if (image.isEmpty()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Request must have a file", false),
					HttpStatus.BAD_REQUEST);
		}

		// uploading the file into server
		this.userService.uploadImage(image, jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<ApiResponse>(new ApiResponse("Profile image uploaded successfully", true),
				HttpStatus.OK);

	}

	// get user info to admin dashboard
	@GetMapping("/")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userService.getUserProfiles());
	}
	
	//get Single user info
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUser(@PathVariable Long userId) {
		User user = userService.getUser(userId);
		if(user == null) {
			return ResponseEntity.badRequest().body("User with " + userId + " not found");
		}
		return ResponseEntity.ok(modelMapper.map(user, UserDto.class));
	}

	// method to serve user profile image
	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path,imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	/**
	 * Food Management
	 */

	
}
