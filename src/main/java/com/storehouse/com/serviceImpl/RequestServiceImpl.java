package com.storehouse.com.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.dto.RequestDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.RequestStatus;
import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.StoreProduct;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.BadRequestException;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.RequestRepository;
import com.storehouse.com.repository.StoreProductRepository;
import com.storehouse.com.repository.StoreRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.RequestService;
import com.storehouse.com.status.ProductStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Service
public class RequestServiceImpl implements RequestService {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductRepository productrepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreProductRepository storeproductRepository;
	
	@Autowired
	private ProductRepository productRepository;
@Autowired
	private StoreRepository storeRepository;
@Value("${stripe.api.key}")
private String stripeApiKey;
@Autowired
private Environment environment;
	@Override
	public List<RequestDto> getAllRequest(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		List<Request> requestList = requestRepository.findAll();
		return requestList.stream().map((s) -> this.modelMapper.map(s, RequestDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<RequestDto> getAllRequest(Long productId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		List<Request> requestList = requestRepository.findRequestByProductProductId(productId);
		List<RequestDto> requestDtos = requestList.stream()
				.map((product) -> this.modelMapper.map(product, RequestDto.class)).collect(Collectors.toList());
		return requestDtos;
	}

	@Override
	public List<RequestDto> getAllRequestByProducer(String token, Long producerId) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));

		List<Request> requestList = requestRepository.findByProducer(account.getUser());

		List<RequestDto> requestDtos = requestList.stream()
				.map((product) -> this.modelMapper.map(product, RequestDto.class)).collect(Collectors.toList());
		return requestDtos;
	}

	@Override
	public List<RequestDto> getSenesRequest(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		List<Request> requestList = null;

		List<RequestDto> requestDtos = requestList.stream()
				.map((product) -> this.modelMapper.map(product, RequestDto.class)).collect(Collectors.toList());
		return requestDtos;
	}

	@Override
	public Request getProductRequestById(Long requestId) {
		Request request = requestRepository.findById(requestId)
				.orElseThrow(() -> new ResourceNotFoundException("Reques", "Request Id", requestId.toString()));
		System.out.println("////////////requestrequestrequest////////////////" + request);

		return request;
	}

	@Override
	public boolean acceptProductRequest(Long requestId, String jwtFromRequest) {
		Optional<Request> requestOptional = requestRepository.findById(requestId);

		if (!requestOptional.isPresent()) {
			return false;
		}
		Request request = requestOptional.get();

		if (!RequestStatus.PENDING.name().equals(request.getStatus())) {

			return false;
		}

		request.setStatus(RequestStatus.ACCEPTED.name());
		System.out.println("middle phase");
		requestRepository.save(request);

		return true;
	}

	@Override
	public List<RequestDto> getAllAcceptedRequestForStoreManager(Long storeManagerId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		
		return requestRepository.findByStatusAndStoreManager(RequestStatus.ACCEPTED.name(), account.getUser()).stream()
				.map((store) -> this.modelMapper.map(store, RequestDto.class)).collect(Collectors.toList());

	}
	
//ure dwlierr
	@Override
	public Request getProductByRequestId(Long requestId, HttpServletRequest request) {

		return null;
	}

	@Override
	public Request getProductByRequestId(String token, Long requestId, Long productId) {

		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		User user = account.getUser();
		Product product = productrepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId.toString()));

		Request requestproductId = requestRepository.findProductByRequestId(product.getProductId());

		return requestproductId;
	}

	@Override
	public Request addProductToStre(Long productId, Long storeManagerId, String token) {
		Product product = getProductById(productId);
		User storemanager = userRepository.findById(storeManagerId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", storeManagerId.toString()));
		product.setStoreManager(storemanager);
		product.setStatus(ProductStatus.ACCEPTED.name());
		Product productrequest = productrepository.save(product);
		Request requestproduct = requestRepository.save(productrequest);
		return requestproduct;

	}

	private Product getProductById(Long productId) {
		return productrepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

	}

	@Override
	public Request incrementQuantity(Long requestId, String token) {
		Request productRequest = requestRepository.findById(requestId).orElse(null);
		if (productRequest == null) {
			throw new IllegalArgumentException("Inavlid Requst id");
		}
		productRequest.setQuantity(productRequest.getQuantity() + 1);

		return requestRepository.save(productRequest);
	}

	@Override
	public Request decremeantQuantity(Long requestId, String token) {
		Request productRequest = requestRepository.findById(requestId).orElse(null);
		if (productRequest == null) {
			throw new IllegalArgumentException("Request Id Not Found");
		}
		if (productRequest.getQuantity() == 0) {
			throw new IllegalArgumentException("Request Id is less thanzero or equal");

		}
		productRequest.setQuantity(productRequest.getQuantity() - 1);
		return requestRepository.save(productRequest);
	}

//	this implementtion is for send request to producer.now it is fact thathat in product entiyt als awill be sennt a confirmation
	@Override
	public Request addRequest(RequestDto requestDto, Long productId, Long producerId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user = account.getUser();
		Optional<Product> productOptional = productRepository.findById(productId);
		Product product = productOptional.get();
		
		product.setProducer(account.getUser());
		product.setUser(user);
		// Product for product
		product.setRequestSent(true);
		product.setProducer(account.getUser());

		productRepository.save(product);

		Request productRequest = this.modelMapper.map(requestDto, Request.class);
		productRequest.setStatus(RequestStatus.PENDING.name());

		productRequest.setQuantity(requestDto.getQuantity());
		productRequest.setDescription(requestDto.getDescription());
		productRequest.setTotalPrice(requestDto.getQuantity() * product.getPrice());
		productRequest.setProduct(product);
		productRequest.setProducer(account.getUser());
		productRequest.setStoreManager(account.getUser());
		return this.requestRepository.save(productRequest);
	}

	@Override
	public Request requestpaymentcomplete(RequestDto requestDto, Long productId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		
		User user=account.getUser();
		Optional<Product> productOptional = productRepository.findById(productId);
		Product product = productOptional.get();
		
		product.setStoreManager(account.getUser());
		product.setPaymentcomplete(true);
		product.setUser(account.getUser());
		productRepository.save(product);
		
		
		
		
		Request productRequest = this.modelMapper.map(requestDto, Request.class);
		
		productRequest.setProducer(account.getUser());
		productRequest.setStoreManager(account.getUser());
		productRequest.setProduct(product);
		
		return this.requestRepository.save(productRequest);
	}

	@Override
	public Request paymentProduct(Long requestId) throws StripeException {
		String stripeApiKey = environment.getProperty("stripe.api.key");
		Stripe.apiKey = stripeApiKey;

		Request request=requestRepository.findById(requestId).orElseThrow(()->new ResourceNotFoundException("Request","Request Id",requestId.toString()));
		
		System.out.println("");
		Map<String, Object> params = new HashMap<>();
		params.put("amount", Math.round(request.getTotalPrice() * 100));
		params.put("currency", "BDT");
		params.put("payment_method_types", Arrays.asList("card"));

		PaymentIntent intent = PaymentIntent.create(params);
request.setPaymentIntentId(intent.getId());

		return requestRepository.save(request);
		
	}



}
