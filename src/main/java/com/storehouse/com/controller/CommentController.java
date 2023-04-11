package com.storehouse.com.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.storehouse.com.dto.ApiResponse;
import com.storehouse.com.dto.CommentDto;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.CommentService;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private CommentService commentService;
	@PostMapping("product/{productId}")
	public ResponseEntity<CommentDto>createcommentProduct(@RequestBody CommentDto commentDto,@PathVariable Long productId,HttpServletRequest request){
		CommentDto newComment=this.commentService.createComment(commentDto,productId,jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<CommentDto>(newComment,HttpStatus.OK);
		
	}
	@PostMapping("store/{storeId}")
	public ResponseEntity<CommentDto>createcommentStore(@RequestBody CommentDto commentDto,@PathVariable Long storeId,HttpServletRequest request){
		CommentDto newComment=this.commentService.createCommentstore(commentDto, storeId,jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<CommentDto>(newComment,HttpStatus.OK);
		
	}
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> createComment(
			@PathVariable Long commentId){
		this.commentService.deleteComment(commentId);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully !!",true),HttpStatus.OK);
		
	}
	
	@PutMapping("/comments/update/{commentId}")
	public ResponseEntity<ApiResponse>updatecomment(@Valid @RequestBody CommentDto  commentDto,@PathVariable Long commentId,HttpServletRequest request){
		this.commentService.updateComment(commentDto,commentId,jwtUtils.getJWTFromRequest(request));
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment updated succesfully",true),HttpStatus.CREATED);
		
	}
	
}
