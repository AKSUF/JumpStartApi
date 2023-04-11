package com.storehouse.com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.storehouse.com.dto.DeliveryAssignmentDto;
import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.dto.RequestDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Delivery;
import com.storehouse.com.entity.DeliveryAssignment;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.DeliveryRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.AccountService;
import com.storehouse.com.service.DeliverService;
import com.storehouse.com.service.OrderService;
import com.storehouse.com.service.ProducerService;
import com.storehouse.com.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/v1/customer")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ProducerService producerservice;
	@Autowired
	private AccountRepository accountrepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Value("${stripe.api.key}")
	private String stripeApiKey;

	@Autowired
	private Environment environment;
	@Autowired
	private DeliverService deliveryService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;
//api for give order
	@PostMapping("/orders/{productId}")
	public ResponseEntity<DeliveryDto> orderProduct(@Valid @RequestBody DeliveryDto deliveryDto,
			@PathVariable Long productId, HttpServletRequest request) throws StripeException {
		String stripeApiKey = environment.getProperty("stripe.api.key");
		Stripe.apiKey = stripeApiKey;
		// Product product = productRepository.getProductById(productId);
		ProductDto product = producerservice.getProductById(productId);
		System.out.println("Product id" + product);
		System.out.println("Product id" + productId);
		// Create a PaymentIntent
		// get product by id

		// set product price as total price
		deliveryDto.setTotalPrice(product.getPrice());
		System.out.println("Product id" + product.getPrice());

		Map<String, Object> params = new HashMap<>();
		System.out.println("Total Price: " + deliveryDto.getTotalPrice());
		params.put("amount", Math.round(deliveryDto.getTotalPrice() * 100));// amount is in taka
		System.out.println("Total Price: " + deliveryDto.getTotalPrice());

		System.out.println("Total Price: " + deliveryDto.getTotalPrice());

		params.put("currency", "BDT");
		params.put("payment_method_types", Arrays.asList("card"));

		PaymentIntent intent = PaymentIntent.create(params);

		// Save the PaymentIntent ID in the order
		deliveryDto.setPaymentIntentId(intent.getId());

		// Place the order
		Delivery orderProduct = orderService.orderProduct(deliveryDto, productId, jwtUtils.getJWTFromRequest(request));
		DeliveryDto orderdelivery = this.modelMapper.map(orderProduct, DeliveryDto.class);
		return new ResponseEntity<DeliveryDto>(orderdelivery, HttpStatus.CREATED);
	}
	
	//api for getallloerder list
	@GetMapping("/orders/getorderlist")
	public ResponseEntity<List<DeliveryDto>>getOrder(HttpServletRequest request){
		List<DeliveryDto> deliverDto=this.deliveryService.getAllOrderlist(jwtUtils.getJWTFromRequest(request));
		
		return new ResponseEntity<List<DeliveryDto>>((List<DeliveryDto>) deliverDto,HttpStatus.OK);
		
	}
	
	@GetMapping("/orders/delivery")
	public ResponseEntity<List<DeliveryDto>>orderr(HttpServletRequest request){
		List<DeliveryDto> deliverDto=this.deliveryService.getAllOrderForDeliveryman(jwtUtils.getJWTFromRequest(request));
		
		return new ResponseEntity<List<DeliveryDto>>((List<DeliveryDto>) deliverDto,HttpStatus.OK);
		
	}
	
	
	
	
	//have to delete
	@GetMapping("/orders/deliveryman")
	public ResponseEntity<List<User>>gedelivermanfromUser(HttpServletRequest request){
		List<User>deliveryman=userService.getUserByRole("ROLE_RIDER",jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<List<User>>(deliveryman,HttpStatus.OK);
		
	}
	//getlist of rider or deliverman
	@GetMapping("/riders")
	public ResponseEntity<?> getRiders(HttpServletRequest request) {
		List<User> users = userrepository.findAll();
		List<Map<String, Object>> producerList = new ArrayList<>();
		users.stream().filter(user -> user.getAccount().getUserRoles().stream()
				.anyMatch(userRole -> userRole.getRole().getRole_name().equals("ROLE_RIDER"))).forEach(user -> {
					Map<String, Object> producer = new HashMap<>();
					producer.put("id", user.getUser_id());
					producer.put("email", user.getAccount().getEmail());
					producer.put("name", user.getAccount().getUser().getName());
//					producer.put("district", user.getAccount().getUser().getDistrict());
//					producer.put("address", user.getAccount().getUser().getAddress());
//					producer.put("Types of vehicles", user.getAccount().getUser().getTypeOfVehicles());
					producerList.add(producer);
				});
		return ResponseEntity.ok(producerList);
	}
	
	
	//getlist of storemanager
	@GetMapping("/storemanager")
	public ResponseEntity<?> storemanager(HttpServletRequest request) {
		List<User> users = userrepository.findAll();
		List<Map<String, Object>> producerList = new ArrayList<>();
		users.stream().filter(user -> user.getAccount().getUserRoles().stream()
				.anyMatch(userRole -> userRole.getRole().getRole_name().equals("ROLE_STOREMANAGER"))).forEach(user -> {
					Map<String, Object> producer = new HashMap<>();
					producer.put("id", user.getUser_id());
					producer.put("email", user.getAccount().getEmail());
					producer.put("name", user.getAccount().getUser().getName());
//					producer.put("district", user.getAccount().getUser().getDistrict());
//					producer.put("address", user.getAccount().getUser().getAddress());
//					producer.put("Types of vehicles", user.getAccount().getUser().getTypeOfVehicles());
					producerList.add(producer);
				});
		return ResponseEntity.ok(producerList);
	}
	
	
	
	
	
	
	
	
	//api for give member in the delivryssign table for deliver
	@PostMapping("/riders/assign/{deliveryId}/{deliverymanId}")
	public ResponseEntity<DeliveryAssignmentDto>addDeliveryAssignment(@RequestBody DeliveryAssignmentDto deliveryAssignmentDto,HttpServletRequest request, @PathVariable Long deliveryId,@PathVariable Long deliverymanId){
		
		DeliveryAssignment orderProduct=orderService.assgindeliveryman(deliveryAssignmentDto,jwtUtils.getJWTFromRequest(request),deliveryId,deliverymanId);
		
		DeliveryAssignmentDto deliveryAssignment=this.modelMapper.map(orderProduct,DeliveryAssignmentDto.class);
		return new ResponseEntity<DeliveryAssignmentDto>(deliveryAssignment,HttpStatus.CREATED);
		
	}
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/newOrder/{productId}")
	public ResponseEntity<DeliveryDto> newOrder(@Valid @RequestBody DeliveryDto deliveryDto,
	                                             @PathVariable Long productId,
	                                             HttpServletRequest request) {
	    String token = jwtUtils.getJWTFromRequest(request);
	    String email = jwtUtils.getUserNameFromToken(token);
	    Account account = accountrepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
	    User user = account.getUser();
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("product", "product is not found", productId.toString()));
	    Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
	    delivery.setCustomer(user);
	    delivery.setProduct(product);
	    delivery.setUser(user);
	    delivery.setStatus("Pending");
	    Delivery savedDelivery = deliveryRepository.save(delivery);
	    DeliveryDto savedDeliveryDto = modelMapper.map(savedDelivery, DeliveryDto.class);
	    return ResponseEntity.status(HttpStatus.CREATED).body(savedDeliveryDto);
	}


//	@PostMapping("/orders/{productId}")
//	public ResponseEntity<DeliveryDto>orderProduct(@Valid @RequestBody DeliveryDto  deliveryDto, @PathVariable Long productId, HttpServletRequest request ){
//		Delivery delivery=this.orderService.orderProduct(deliveryDto, productId, jwtUtils.getJWTFromRequest(request));
//		DeliveryDto newDeliveryDto=this.modelMapper.map(delivery,DeliveryDto.class);
//		return new ResponseEntity<DeliveryDto>(newDeliveryDto,HttpStatus.CREATED);
//		
//	}
//	
//api for accept delivery request
	@PostMapping("/accept/{deliveryId}")
	public ResponseEntity<String>acceptProductOrder(@PathVariable Long deliveryId,HttpServletRequest request){
		Delivery delivery=deliveryService.getDeliveryRequestById(deliveryId);
		if(delivery==null) {
			return new ResponseEntity<>("Delivery not found.", HttpStatus.NOT_FOUND);

		}
		boolean requestAccepted =deliveryService.acceptRequest(deliveryId,jwtUtils.getJWTFromRequest(request));
		if(requestAccepted) {
			return new ResponseEntity<>("Request accepted.", HttpStatus.OK);
		}else {
			
		
			return new ResponseEntity<>("Error accepting request.", HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	
		
	}
	
	
	
	
	@PutMapping("/orders/{deliveryId}")
	public ResponseEntity<DeliveryDto> orderProduct(HttpServletRequest request, @PathVariable Long deliveryId,
			@RequestParam String status) {
		String token = jwtUtils.getJWTFromRequest(request);
		Account account = accountService.getAccount(token);
		User user = account.getUser();
		DeliveryDto orderProduct = this.orderService.orderStatus(user, deliveryId, status);
		return new ResponseEntity<DeliveryDto>(orderProduct, HttpStatus.OK);

	}

	@GetMapping("/orders")
	public ResponseEntity<List<DeliveryDto>> getOrderPProduct(HttpServletRequest request) {
		System.out.println("this code is for get all deliverdetails");
		List<DeliveryDto> orderProduct = this.orderService.userOrders(jwtUtils.getJWTFromRequest(request));
		if (orderProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}
		System.out.println("this code is for get all deliverdetails");
		return ResponseEntity.status(HttpStatus.OK).body(orderProduct);

	}
//to get all acceted request from deliveryman to customer
	@GetMapping("/orders/{customerId}/customer")
	public ResponseEntity<List<DeliveryDto>>getAllacceptedrequest(HttpServletRequest request,Long customerId){
		List<DeliveryDto>acceptedRequest=deliveryService.getAllAccepted(customerId,jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<List<DeliveryDto>>(acceptedRequest,HttpStatus.OK);
		
	}
	
	
	
	
	
	@PostMapping("orderconfirm/{deliveryId}")
	public ResponseEntity<DeliveryDto> riderConfirm(@PathVariable Long deliveryId, HttpServletRequest request) {
		DeliveryDto orderProduct = this.orderService.confirmOrderdelivery(deliveryId,
				jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<DeliveryDto>(orderProduct, HttpStatus.CREATED);

	}

//Drliverymanagement------The store will be manage the delivery thing nobody can not do that other withoiutt his permission

//	@GetMapping("/orders/alluserorder")
//	public ResponseEntity<List<DeliveryDto>> getAllOrderProduct() {
//		List<DeliveryDto> orderProduct = this.orderService.allOrders();
//		if (orderProduct.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(orderProduct);
//
//	}
	@GetMapping("/orders/alluserorder")
	public ResponseEntity<List<DeliveryDto>> getAllUserOrder(HttpServletRequest request) {
		List<DeliveryDto> deliveryDtos = orderService.getAllOrders(jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<>(deliveryDtos, HttpStatus.OK);

	}

	@GetMapping("/ordersuser/{deliveryId}")
	public ResponseEntity<DeliveryDto> getSingleorder(@PathVariable Long deliveryId) {
		DeliveryDto orderProduct = this.orderService.singleorder(deliveryId);

		return ResponseEntity.status(HttpStatus.OK).body(orderProduct);

	}

	@GetMapping("/orders/pendingorder")
	public ResponseEntity<List<DeliveryDto>> getPemdingOrders() {
		return ResponseEntity.ok(this.orderService.pendingOrders());

	}

}
