package com.storehouse.com.serviceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.CommentDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Comment;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Store;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.CommentRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.StoreRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.CommentService;


@Service
public class CommentServiceImpl implements CommentService{
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
private ModelMapper modelmapper;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Override
	public CommentDto createComment(CommentDto commentDto, Long productId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","Product Id",productId.toString()));
		Comment comment=this.modelmapper.map(commentDto, Comment.class);
		comment.setProduct(product);
		Comment saveComment=this.commentRepository.save(comment);
		return this.modelmapper.map(saveComment,CommentDto.class);
	}
	@Override
	public CommentDto createCommentstore(CommentDto commentDto, Long storeId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		Store store=storeRepository.findById(storeId).orElseThrow(()->new ResourceNotFoundException("Store","Store Id",storeId.toString()));
		Comment comment=this.modelmapper.map(commentDto, Comment.class);
		comment.setStore(store);
		Comment saveComment=this.commentRepository.save(comment);
		
		return this.modelmapper.map(saveComment, CommentDto.class);
	}
	@Override
	public void deleteComment(Long commentId) {
//		Comment comment = this.commentRepository.findById(commentId).orElseThrow(()-> new
//				ResourceNotFoundException("Comment", "Id", commentId.toString()));
		
		Comment comment=this.commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","Comment Id",commentId.toString()));
		this.commentRepository.delete(comment);
		
	}
	@Override
	public CommentDto updateComment(@Valid CommentDto commentDto, Long commentId, String token) {
		Comment comment=this.commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","Comment Id",commentId.toString()));
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credntial",email));
		comment.setContent(commentDto.getContent());
		Comment updateComment=this.commentRepository.save(comment);
		
		return this.modelmapper.map(updateComment, CommentDto.class);
	
		
	}

}
