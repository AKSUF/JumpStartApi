package com.storehouse.com.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.storehouse.com.dto.DeliveryAssignmentDto;
import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Delivery;
import com.storehouse.com.entity.DeliveryAssignment;
import com.storehouse.com.entity.DeliveryMan;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.RequestStatus;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.DeliveryAssignmentRepository;
import com.storehouse.com.repository.DeliveryManRepository;
import com.storehouse.com.repository.DeliveryRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private DeliveryRepository deliverrepository;
	@Autowired
	private DeliveryManRepository deliveryManRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DeliveryAssignmentRepository deliveryAssignmentRepository;
	
	@Override
	public Delivery orderProduct(@Valid DeliveryDto deliveryDto, Long productId, String token) {
		Product product = this.productRepository.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("product", "product is not found", productId.toString()));
		
		System.out.println("////////ServiceImpl////user.user()//////////"+product);
		System.out.println("////////ServiceImpl///user.user()//////////"+product);
		String email = jwtUtils.getUserNameFromToken(token);
		System.out.println("////////////uServiceImpl//////////"+email);
		System.out.println("////////////user.user()//////////"+email);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		System.out.println("////////////user.user()//////////"+account);
		
		Delivery delivery = this.modelMapper.map(deliveryDto, Delivery.class);
		
		System.out.println("//////ServiceImpl/user.user()//////////"+delivery);
		delivery.setDeliveryNumber((int) (Math.floor(Math.random() * (99999999 - 00000000)) + 00000000));
		delivery.setQuantity((int) (Math.floor(Math.random() * (99999999 - 00000000)) + 00000000));
	
		delivery.setStatus(RequestStatus.PENDING.name());
User user=account.getUser();
		
		System.out.println("////ServiceImpl/////////"+account.getUser());
		System.out.println("////////////account.getUser()//////////"+account.getUser());
		System.out.println("////////////user.user()//////////"+user);
		delivery.setUser(user);
		delivery.setCustomer(user);
	
		delivery.setProduct(product);


		return this.deliverrepository.save(delivery);
	}

	@Override
	public DeliveryDto orderStatus(User user, Long deliveryId, String status) {
		Delivery delivery = this.deliverrepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("delivery", "delivery id", deliveryId.toString()));
		delivery.setStatus(status);
		System.out.println(status);
		System.out.println("///////////////////////");
		if (status.equalsIgnoreCase("Ordered")) {
			DeliveryMan deliverman = new DeliveryMan();
			deliverman.setUserdelivryman(user);
			deliverman.setDelivery(delivery);
			this.deliveryManRepository.save(deliverman);
		}
		Delivery changeStatus = this.deliverrepository.save(delivery);

		return this.modelMapper.map(changeStatus, DeliveryDto.class);
	}

	// get all delivery deatils
	@Override
	public List<DeliveryDto> userOrders(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		List<Delivery> userOrders = this.deliverrepository.findByUser(account.getUser());
		List<DeliveryDto> orderDtos = userOrders.stream().map(order -> this.modelMapper.map(order, DeliveryDto.class))
				.collect(Collectors.toList());

		return orderDtos;
	}

	@Override
	public DeliveryDto confirmOrderdelivery(Long deliveryId, String token) {
		Delivery orderderProduct = this.deliverrepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("deliverry", "delivery Id", deliveryId.toString()));
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		DeliveryMan deliveryMan = new DeliveryMan();
		deliveryMan.setUserdelivryman(account.getUser());
		deliveryMan.setDelivery(orderderProduct);
		deliveryMan = deliveryManRepository.save(deliveryMan);
		return this.modelMapper.map(orderderProduct, DeliveryDto.class);

	}

	// get all product order

	@Override
	public List<DeliveryDto> allOrders() {
		List<Delivery> userOrders = this.deliverrepository.findAll();
		List<DeliveryDto> orderDtos = userOrders.stream().map(order -> this.modelMapper.map(order, DeliveryDto.class))
				.collect(Collectors.toList());

		return orderDtos;
	}

	@Override
	public DeliveryDto singleorder(Long deliveryId) {
		Delivery deliver = this.deliverrepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("delivery", "delivery id", deliveryId.toString()));
		return this.modelMapper.map(deliver, DeliveryDto.class);
	}

	@Override
	public List<DeliveryDto> pendingOrders() {
		List<DeliveryDto> deliveryDtos = this.deliverrepository.findByStatus("PENDING").stream()
				.map((delivery) -> this.modelMapper.map(delivery, DeliveryDto.class)).collect(Collectors.toList());
		return deliveryDtos;
	}

	@Override
	public List<DeliveryDto> getAllOrders(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		User user=account.getUser();
List<Delivery>deliveryDeliveries=deliverrepository.findAll();

		return deliveryDeliveries.stream().map((s)->this.modelMapper.map(s, DeliveryDto.class)).collect(Collectors.toList());
	}

	@Override
	public Delivery newOrder(@Valid DeliveryDto deliveryDto, Long productId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Product product = this.productRepository.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("product", "product is not found", productId.toString()));
		

		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
User user=account.getUser();
		Delivery delivery=this.modelMapper.map(deliveryDto,Delivery.class);
		delivery.setCustomer(user);
		delivery.setProduct(product);
		delivery.setUser(user);
		return this.deliverrepository.save(delivery);
	}

	@Override
	public DeliveryAssignment assgindeliveryman(DeliveryAssignmentDto deliveryAssignmentDto, String token, Long deliveryId,
			Long deliverymanId) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepo.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
		
		User user=account.getUser();
		
	
		User deliveryman=userRepository.findById(deliverymanId).orElseThrow(()->new ResourceNotFoundException("User","User Id",deliverymanId.toString()));
		Delivery delivery=deliverrepository.findById(deliveryId).orElseThrow(()->new ResourceNotFoundException("Delivery","Delivery Id",deliveryId.toString()));
	
		DeliveryAssignment deliveryAssignment=this.modelMapper.map(deliveryAssignmentDto, DeliveryAssignment.class);
	deliveryAssignment.setAssignedBy(user);
	deliveryAssignment.setDeliveryman(deliveryman);
	deliveryAssignment.setDelivery(delivery);

		return this.deliveryAssignmentRepository.save(deliveryAssignment);
	}

//	@Override
//	public Delivery orderProduct(@Valid DeliveryDto deliveryDto, Long productId, String jwtFromRequest) {
//		String email = jwtUtils.getUserNameFromToken(jwtFromRequest);
//		Account account = accountRepo.findByEmail(email)
//				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
//		Product product = this.productRepository.findById(productId).orElseThrow(
//				() -> new ResourceNotFoundException("product", "product is not found", productId.toString()));
//
//Delivery delivery=this.modelMapper.map(deliveryDto, Delivery.class);
//delivery.setDelivery_number((int) (Math.floor(Math.random() * (99999999 - 00000000)) + 00000000));
//delivery.setQuantity((int) (Math.floor(Math.random() * (99999999 - 00000000)) + 00000000));
//delivery.setStatus("Pending");
//User user=account.getUser();
//
//System.out.println("////////////account.getUser()//////////"+account.getUser());
//System.out.println("////////////account.getUser()//////////"+account.getUser());
//System.out.println("////////////user.user()//////////"+user);
//delivery.setUser(user);
//delivery.setCustomer(user);
//delivery.setDeliveryMan(user);
//delivery.setProduct(product);
//
//		return this.deliverrepository.save(delivery);
//	}

//	// this method has problem
//	@Override
//	public List<DeliveryDto> delivermanConfirmorders(String token) {
//
//	String email = jwtUtils.getUserNameFromToken(token);
//	Account account = accountRepo.findByEmail(email)
//			.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));
//	User deliveryman = account.getUser();
//		List<DeliveryDto> deliveryDto = new ArrayList<DeliveryDto>();
//		deliveryman.getDeliveryMan().stream().forEach((delivery)->{
//			deliveryDto.add(this.modelMapper.map(delivery. , destinationType))
//	})
//	
//		deliveryman.getDeliveryMan().stream().forEach((delivery) -> {
//		deliveryDto.add(this.modelMapper.map(delivery.getDelivery(), DeliveryDto.class));
//	});
//
//	
//		return deliveryDto;
//	}

}
