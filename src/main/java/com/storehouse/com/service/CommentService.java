package com.storehouse.com.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.storehouse.com.dto.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto, Long productId, String token);

	CommentDto createCommentstore(CommentDto commentDto, Long storeId, String token);

	void deleteComment(Long commentId);

	CommentDto updateComment(@Valid CommentDto commentDto, Long commentId, String token);

}
