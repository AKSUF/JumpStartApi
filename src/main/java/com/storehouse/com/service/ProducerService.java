package com.storehouse.com.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.dto.RequestDto;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.User;

public interface ProducerService {

	Product addProduct(@Valid ProductDto productDto, String jwtFromRequest);

	ProductDto updateaddedProduct(@Valid ProductDto productDto, String jwtFromRequest, Long productId);

	ProductDto addnewProduct(ProductDto productDto,Long storeId, String token);

	ProductDto getProductById(Long productId);

	List<ProductDto> getProductByUser(Long userId, String jwtFromRequest);

	List<ProductDto> getProductAsStore( String token);

	List<ProductDto> getAllProducts(String token);

	Product getProductByProductId(Long productId);

	User getUserById(Long producerId, String token);

	boolean sendProductRequest(Long productId, Long producerId, String token);

	List<ProductDto> getPrdetail(String token);



	

}
