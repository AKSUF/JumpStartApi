package com.storehouse.com.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.dto.RequestDto;
import com.storehouse.com.dto.StoreProductDto;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.RequestStatus;
import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.StoreProduct;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.StoreProductRepository;
import com.storehouse.com.repository.StoreRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.ProducerService;
import com.storehouse.com.service.RequestService;
import com.storehouse.com.service.StoresService;
import com.storehouse.com.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/v1/storemanager")
public class StoreManagecontroller {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProducerService producerService;
	@Autowired
	private RequestService requestService;
	@Value("${stripe.api.key}")
	private String stripeApiKey;
	@Autowired
	private StoreProductRepository storeproductRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private Environment environment;
	@Autowired
	private StoresService storeService;
	
	@PostMapping("/products/{productId}/request")
	public ResponseEntity<RequestDto> sensRequestoProducer(@RequestBody RequestDto requestDto,
			@PathVariable Long productId, Long producerId, HttpServletRequest request) {

//		boolean requesSent = producerService.sendProductRequest(productId, producerId,
//				jwtUtils.getJWTFromRequest(request));

		Request productrequest = requestService.addRequest(requestDto, productId, producerId,
				jwtUtils.getJWTFromRequest(request));

		RequestDto newrequest = this.modelMapper.map(productrequest, RequestDto.class);

		return new ResponseEntity<RequestDto>(newrequest, HttpStatus.CREATED);

	}

	// API to get all products
	@GetMapping("/products")
	public ResponseEntity<List<ProductDto>> getAllProducts(HttpServletRequest request) {
		List<ProductDto> productList = producerService.getAllProducts(jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}

	// api for send request to producer acccept storemanager request

	@PostMapping("/products/{productId}/request/hj")
	public ResponseEntity<String> sendProductRequest(@PathVariable Long productId, Long producerId,
			HttpServletRequest request) {
		Product product = producerService.getProductByProductId(productId);

		if (product == null) {
			return new ResponseEntity<>("Product not found", HttpStatus.OK);
		}
		User producer = producerService.getUserById(producerId, jwtUtils.getJWTFromRequest(request));

		if (producer == null) {
			return new ResponseEntity<>("Producer not found", HttpStatus.NOT_FOUND);
		}

		boolean requesSent = producerService.sendProductRequest(productId, producerId,
				jwtUtils.getJWTFromRequest(request));
		if (requesSent) {
			System.out.println("//////////////////444444444444444444444444444444444//////////");
			return ResponseEntity.ok("Request Sent to Producer");

		} else {
			System.out.println("/////////////55555555555555555555555555555555555555555///////////////");
			return ResponseEntity.badRequest().body("Error sending request");
		}
	}

//api to get all request from storemanager 
	@GetMapping("/request")
	public ResponseEntity<List<RequestDto>> getallrequest(HttpServletRequest request) {
		List<RequestDto> getallrequest = requestService.getAllRequest(jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<>(getallrequest, HttpStatus.OK);

	}

	// api to get all request from storemanager as product id
	@GetMapping("/request/product/{productId}")
	public ResponseEntity<List<RequestDto>> getProductRequest(@PathVariable Long productId,
			HttpServletRequest request) {
		Product product = producerService.getProductByProductId(productId);
		if (product == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<RequestDto> productRequest = requestService.getAllRequest(productId, jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<List<RequestDto>>(productRequest, HttpStatus.OK);

	}

	// api to get all request from storemanager as producer
	@GetMapping("/request/producer")
	public ResponseEntity<List<RequestDto>> getProductByProducer(HttpServletRequest request, Long producerId) {
		List<RequestDto> productRequest = requestService.getAllRequestByProducer(jwtUtils.getJWTFromRequest(request),
				producerId);
		return new ResponseEntity<List<RequestDto>>(productRequest, HttpStatus.OK);

	}

	// api to get all request from storemanager as storemanager
	@GetMapping("/request/request")
	public ResponseEntity<List<RequestDto>> getRequestAsStore(HttpServletRequest request) {
		List<RequestDto> storerequest = requestService.getSenesRequest(jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<List<RequestDto>>(storerequest, HttpStatus.OK);
	}

	// api to accept request byb storemanager
	@PostMapping("/request/{requestId}/accept")
	public ResponseEntity<String> acceptProductRequest(@PathVariable Long requestId, HttpServletRequest request) {
		Request productrequest = requestService.getProductRequestById(requestId);
		System.out.println("productrequest" + productrequest);
		if (productrequest == null) {
			return new ResponseEntity<>("Request not found.", HttpStatus.NOT_FOUND);
		}
		boolean requestAccepted = requestService.acceptProductRequest(requestId, jwtUtils.getJWTFromRequest(request));

		if (requestAccepted) {

			return new ResponseEntity<>("Request accepted.", HttpStatus.OK);

		} else {
			return new ResponseEntity<>("Error accepting request.", HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	
	
	
	
	// api to get accepted request for a storemanager
	
	@GetMapping("request/{storeManagerId}/acceptedrequest")
	public ResponseEntity<List<RequestDto>> getAllAcceptedRequest(HttpServletRequest request, Long storeManagerId) {
		List<RequestDto> acceptedRequest = requestService.getAllAcceptedRequestForStoreManager(storeManagerId,
				jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<List<RequestDto>>(acceptedRequest, HttpStatus.OK);

	}

	
	
	
	
	
	
	@PostMapping("/request/products/{productId}/storemanagers")
	public ResponseEntity<RequestDto> addProductToStore(@PathVariable Long productId, Long storeManagerId,
			HttpServletRequest request) {

		return null;
	}

	// api for increase quntity
	@PutMapping("/requests/{requestId}/incrementQuuantity")
	public ResponseEntity<?> incrementQuuantity(@PathVariable Long requestId, HttpServletRequest request) {
		try {
			Request productRequest = requestService.incrementQuantity(requestId, jwtUtils.getJWTFromRequest(request));
			return ResponseEntity.ok(productRequest);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to increament request");
		}
	}

	@PutMapping("/requests/{requestId}/decrementQuantity")
	public ResponseEntity<?> decrementRequestEntity(@PathVariable Long requestId, HttpServletRequest request) {
		try {
			Request productRequest = requestService.decremeantQuantity(requestId, jwtUtils.getJWTFromRequest(request));
			return ResponseEntity.ok(productRequest);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to decrement request quantity");

		}

	}

	// payment for give for give payment
	@PostMapping("/request/{productId}/payment")
	public ResponseEntity<RequestDto> completepayment(@RequestBody RequestDto requestDto, @PathVariable Long productId,
			HttpServletRequest request, Long storeId) throws StripeException {
		String stripeApiKey = environment.getProperty("stripe.api.key");
		Stripe.apiKey = stripeApiKey;

		Product product = producerService.getProductByProductId(productId);

		Map<String, Object> params = new HashMap<>();

		int totalprice = (int) (requestDto.getTotalPrice() * 100);

		params.put("amount", Math.round(totalprice));

		System.out.println("///////requestDto.getTotalPrice()*100)//////" + requestDto.getTotalPrice() * 100);

		params.put("currency", "BDT");
		params.put("payment_method_types", Arrays.asList("card"));
		PaymentIntent intent = PaymentIntent.create(params);
		requestDto.setPaymentIntentId(intent.getId());
//to get product id & save in storproduct//must delete
		Product productstore = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId.toString()));
		
		

		Store store = storeRepository.findStoreByStoreManagerId(storeId);

		StoreProduct storeProduct = new StoreProduct();

		storeProduct.setProduct(productstore);
		storeProduct.setStore(store);
		storeproductRepository.save(storeProduct);
		//to get product id & save in storproduct
		
		//complete payment
		
		Request paymentRequestDto = this.requestService.requestpaymentcomplete(requestDto, productId,
				jwtUtils.getJWTFromRequest(request));
		RequestDto paypayment = this.modelMapper.map(paymentRequestDto, RequestDto.class);

		return new ResponseEntity<RequestDto>(paypayment, HttpStatus.CREATED);

	}
	
	
	
	@PostMapping("/requestproductbuy/{productId}/payment/{requestId}")
public ResponseEntity<StoreProductDto>storeproduct(@RequestBody StoreProductDto  storeProductDto,@PathVariable Long productId, Long storeId,HttpServletRequest request,@PathVariable Long requestId) throws Exception{

	
	StoreProduct storeProduct=this.storeService.sendPaymentProduct(storeProductDto,productId,storeId,jwtUtils.getJWTFromRequest(request),requestId);
	StoreProductDto paymentStoreProduct=this.modelMapper.map(storeProduct, StoreProductDto.class);
	return new ResponseEntity<StoreProductDto>(paymentStoreProduct,HttpStatus.CREATED);
	
}


	
	
	
	
	
}
