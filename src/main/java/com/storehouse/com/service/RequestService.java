package com.storehouse.com.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.storehouse.com.dto.RequestDto;
import com.storehouse.com.entity.Request;
import com.stripe.exception.StripeException;

public interface RequestService {

	List<RequestDto> getAllRequest(String token);

	List<RequestDto> getAllRequest(Long productId, String token);

	List<RequestDto> getAllRequestByProducer(String token, Long producerId);

	List<RequestDto> getSenesRequest(String token);

	Request getProductRequestById(Long requestId);

	boolean acceptProductRequest(Long requestId, String jwtFromRequest);

	List<RequestDto> getAllAcceptedRequestForStoreManager(Long storeManagerId, String token);

	Request getProductByRequestId(Long requestId, HttpServletRequest request);



	Request getProductByRequestId(String token, Long requestId, Long productId);

	Request addProductToStre(Long productId, Long storeManagerId, String token);

	Request incrementQuantity(Long requestId, String token);

	Request decremeantQuantity(Long requestId, String token);

	Request addRequest(RequestDto requestDto, Long productId, Long producerId, String token);

	Request requestpaymentcomplete(RequestDto requestDto, Long productId,  String token);

	Request paymentProduct(Long requestId) throws StripeException;





}
