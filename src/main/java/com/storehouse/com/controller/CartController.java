package com.storehouse.com.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storehouse.com.dto.CartDto;
import com.storehouse.com.dto.CartItemDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Cart;
import com.storehouse.com.entity.CartItem;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.CartRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
	@Autowired
	private JwtUtils jwtUtils;
	 @Autowired
	    private CartService cartService;
	 @Autowired
	 private ModelMapper modelmapper;
	 @Autowired
	 private CartRepository cartRepository;
	 
	 ///Cart 
	 @PostMapping("/{productId}")
	 public ResponseEntity<CartDto>addToCart(@PathVariable Long  productId,HttpServletRequest request,@RequestBody CartItemDto cartItemDto){
		 System.out.println("////////This is for cart ///for customer");
		 
		 CartDto cartDto=cartService.addCart(productId,jwtUtils.getJWTFromRequest(request),cartItemDto);
			System.out.println(cartDto+"//////////////////////////////////cartdto");
			System.out.println(cartDto+"//////////////////////////////////cartdto");
		return ResponseEntity.ok(cartDto);
		 
	 }
	 
	 
	 
	@GetMapping("/singlecart/{cartId}")
	public ResponseEntity<CartDto>getCart(@PathVariable Long cartId,HttpServletRequest request) {
		CartDto cartDto=cartService.getCart(cartId,jwtUtils.getJWTFromRequest(request));
		return ResponseEntity.ok(cartDto);
		
	}

	
@GetMapping("/cart/getallitem")	
public ResponseEntity<List<CartDto>>getAllCartByUser(HttpServletRequest request){
	
	List<CartDto>cartDtos=this.cartService.getCartByUser(jwtUtils.getJWTFromRequest(request));
	

	
	return new  ResponseEntity<List<CartDto>>(cartDtos,HttpStatus.OK);
	
}
	
	
	
	@PutMapping("/cartItem/{cartItemId}")
	 public ResponseEntity<CartDto>updateCartItem(@PathVariable Long cartItemId,HttpServletRequest request){
		CartDto cartDto=cartService.updateCartItem(cartItemId,jwtUtils.getJWTFromRequest(request));
	     return ResponseEntity.ok(cartDto);
		
	}
	
    @DeleteMapping("/cartItem/{cartItemId}")
	public ResponseEntity<Void>removeCartItem(@PathVariable Long cartItemId,HttpServletRequest request){
    	   cartService.removeCartItem(cartItemId,jwtUtils.getJWTFromRequest(request));
    	   System.out.println("////////This is for cart delte ///for customer");
    	   return ResponseEntity.noContent().build();
    	
    }

    @PutMapping("/updatequantity")
    public ResponseEntity<CartItem> updateCartItemQuantity(@RequestBody CartItem updateCartRequest,HttpServletRequest request) {
        cartService.updateCartItemQuantity(updateCartRequest.getCartitemId(), updateCartRequest.getQuantity(),jwtUtils.getJWTFromRequest(request));
        
        
        System.out.println("This the quantiy ");
        return ResponseEntity.ok().build();
    }
    
    
    
}
