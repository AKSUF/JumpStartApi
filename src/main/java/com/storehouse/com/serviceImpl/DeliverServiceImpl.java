package com.storehouse.com.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.storehouse.com.dto.DeliveryDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Delivery;
import com.storehouse.com.entity.RequestStatus;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.DeliveryRepository;
import com.storehouse.com.repository.ProducerRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.RequestRepository;
import com.storehouse.com.repository.StoreRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.DeliverService;
import com.storehouse.com.service.UserService;
@Service
public class DeliverServiceImpl implements DeliverService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProducerRepository producerRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Autowired
	private UserService userService;
	/**
	 * Returns a list of DeliveryDto objects representing all delivery orders if the user's address matches the delivery address.
	 * Otherwise, returns an empty list.
	 * 
	 * @param token the JWT authentication token
	 * @return a list of DeliveryDto objects
	 * @throws ResourceNotFoundException if the user's account cannot be found
	 */
	
	
	@Override
	public List<DeliveryDto> getAllOrderForDeliveryman(String token) {
	    String email = jwtUtils.getUserNameFromToken(token);
	    Account account = accountRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
	    User user=account.getUser();
	    List<Delivery> deliveryList = this.deliveryRepository.findAll();
	    

	    List<DeliveryDto> deliveryDtoList = deliveryList.stream()
	            .filter(delivery -> user.getAdress().equals(delivery.getAddress()))
	            .map(delivery -> this.modelMapper.map(delivery, DeliveryDto.class))
	            .collect(Collectors.toList());

	    return deliveryDtoList;
	}

	
	
	
	
	
	
	@Override
	public List<DeliveryDto> getAllOrderlist(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		List<Delivery>delivery=this.deliveryRepository.findAll();
List<DeliveryDto>deliveryDtos=delivery.stream().map((deliver)->this.modelMapper.map(deliver, DeliveryDto.class)).collect(Collectors.toList());
		return deliveryDtos;
	}







	@Override
	public Delivery getDeliveryRequestById(Long deliveryId) {
		Delivery delivery=deliveryRepository.findById(deliveryId).orElseThrow(()->new ResourceNotFoundException("Delivery","delivery Id",deliveryId.toString()));
		
		return delivery;
	}







	@Override
	public boolean acceptRequest(Long deliveryId, String token) {
		Optional<Delivery>requestoptinal=deliveryRepository.findById(deliveryId);
		System.out.println("///////////////////////requestoptinalImpl//////////////////"+requestoptinal);
		System.out.println("///////////////////////requestoptinalImpl//////////////////"+requestoptinal);
		System.out.println("///////////////////////requestoptinalImpl//////////////////"+requestoptinal);
		if(!requestoptinal.isPresent()) {
			return false;
			
		}
		System.out.println("///////////////////////requestoptinalImpl//////////////////"+requestoptinal.isPresent());
		Delivery delivery=requestoptinal.get();
		System.out.println("///////////////////////deliverylImpl//////////////////"+delivery);
		System.out.println("///////////////////////deliverylImpl//////////////////"+delivery);
		System.out.println("///////////////////////deliverylImpl//////////////////"+delivery);
		if(RequestStatus.PENDING.equals(delivery.getStatus())) {
			return false;
		}
		
		delivery.setStatus(RequestStatus.ACCEPTED.name());
		deliveryRepository.save(delivery);
		System.out.println("///////////////////////deliverylImpl//////////////////");
		return true;
			
	}







	@Override
	public List<DeliveryDto> getAllAccepted(Long customerId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));

		return deliveryRepository.findByStatusAndCustomer(RequestStatus.ACCEPTED.name(), account.getUser()).stream()
				.map((store) -> this.modelMapper.map(store, DeliveryDto.class)).collect(Collectors.toList());

	}
	
	
	

}
