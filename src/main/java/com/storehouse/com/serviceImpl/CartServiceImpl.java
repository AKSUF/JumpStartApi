package com.storehouse.com.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.CartDto;
import com.storehouse.com.dto.CartItemDto;
import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Cart;
import com.storehouse.com.entity.CartItem;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.CartItemRepository;
import com.storehouse.com.repository.CartRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.CartService;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CartRepository cartRepository;
	@Autowired
	private JwtUtils jwtUtils;
 
    
	@Autowired
	private ModelMapper modelmapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
private CartItemRepository cartItemRepository;
	@Override
	public CartDto addCart(Long productId, String token, CartItemDto cartItemDto) {
		
	
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credential",email));
		User user=account.getUser();
	
	Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product","productId",productId.toString()));
	//get the cart or create new one
	 
	if(user.getCart() == null) {
		   //If user does not have a cart, create a new cart for the user
		   Cart cart = new Cart();
		   cart.setUser(user);
		   user.setCart(cart);
		   userRepository.save(user); //save the user object to update the changes in the database
		}

	
	Cart cart=user.getCart();
	
  
    if (userRepository != null && user != null) {
        userRepository.save(user);
    }
    CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);

    if (cartItem == null) {
        // Create a new cart item
        cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItemRepository.save(cartItem);
        cart.getCartItems().add(cartItem);
    } else {
        System.out.println("Your cart");
    }

  
    cartRepository.save(cart);
   return modelmapper.map(cart, CartDto.class);

}
	@Override
	public CartDto getCart(Long cartId, String token) {
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credential",email));
		User user=account.getUser();
	    Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId.toString()));
        
        return modelmapper.map(cart, CartDto.class);
		
	}
	@Override
	public CartDto updateCartItem(Long cartItemId, String token) {
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credential",email));
		User user=account.getUser();
        // Find the cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item", "id", cartItemId.toString()));
        // Save the changes
        cartItemRepository.save(cartItem);
		return modelmapper.map(cartItem, CartDto.class);
	}
	@Override
	public void removeCartItem(Long cartItemId, String token) {
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credential",email));
		User user=account.getUser();
		  CartItem cartItem = cartItemRepository.findById(cartItemId)
	                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId.toString()));
	        cartItemRepository.delete(cartItem);
	    }
	@Override
	public CartDto getUserCart(String token) {
		String email=jwtUtils.getUserNameFromToken(token);
		Account account=accountRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user","credential",email));
		User user=account.getUser();
		
		return null;
	}

	@Override
	public List<CartDto> getCartByUser(String token) {
	    String email = jwtUtils.getUserNameFromToken(token);
	    System.out.println(token+"///////////////////////tofghdfghdgsdfsfas////////////////////////////////////////tokk"+email);
	    Account account = accountRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
	    User user = account.getUser();
	    System.out.println(user+"///////////////////////user.user();//////////////account.getUser();/////////////////////////tokk"+account.getUser());
	    System.out.println(user+"///////////////////////user.user();//////////////account.getUser();/////////////////////////tokk"+account.getUser());
	    Cart cart = user.getCart();
	    System.out.println(cart+"///////////////////////user.getCart();////////////////////////////////////////tokk"+user.getCart());
	    System.out.println(cart+"///////////////////////user.getCart();////////////////////////////////////////tokk"+user.getCart());
	    List<CartItem> cartItems = cart.getCartItems();
	    List<CartDto> cartDtos = new ArrayList<>();
	    for (CartItem cartItem : cartItems) {
	        Product product = cartItem.getProduct();
	        CartItemDto cartItemDto = modelmapper.map(cartItem, CartItemDto.class);
	        ProductDto productDto = modelmapper.map(product, ProductDto.class);
	        cartItemDto.setProduct(productDto);
	        CartDto cartDto = new CartDto();
	        cartDto.setCartId(cart.getCartId());
	        cartDto.setCartItems(Collections.singletonList(cartItemDto));
	        cartDtos.add(cartDto);
	    }
	    return cartDtos;
	}
	@Override
	public void updateCartItemQuantity(Long cartitemId, int quantity,String token) {
		
		 String email = jwtUtils.getUserNameFromToken(token);
				 
				  Account account = accountRepository.findByEmail(email)
	                .orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
	        User user = account.getUser();
	        
	        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartitemId);

	        if (cartItemOptional.isPresent()) {
	            CartItem cartItem = cartItemOptional.get();
	            if (cartItem.getCart().getUser().equals(user)) {
	                cartItem.setQuantity(quantity);
	                cartItemRepository.save(cartItem);
	               
	                
	            } else {
	                throw new AccessDeniedException("You do not have permission to modify this cart item.");
	               
	            }
	        } else {
	            throw new IllegalArgumentException("Invalid cart item ID.");
	            
	        }
	    
	}
	
	
	}
	
	
	
	
		
	
	