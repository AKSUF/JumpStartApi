package com.storehouse.com.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.openid.connect.sdk.assurance.Status;
import com.storehouse.com.dto.ProductDto;
import com.storehouse.com.entity.Account;
import com.storehouse.com.entity.Producer;
import com.storehouse.com.entity.Product;
import com.storehouse.com.entity.Request;
import com.storehouse.com.entity.RequestStatus;
import com.storehouse.com.entity.Store;
import com.storehouse.com.entity.StoreManager;
import com.storehouse.com.entity.User;
import com.storehouse.com.exceptions.ResourceNotFoundException;
import com.storehouse.com.repository.AccountRepository;
import com.storehouse.com.repository.ProducerRepository;
import com.storehouse.com.repository.ProductRepository;
import com.storehouse.com.repository.RequestRepository;
import com.storehouse.com.repository.StoreRepository;
import com.storehouse.com.repository.UserRepository;
import com.storehouse.com.security.oath.JwtUtils;
import com.storehouse.com.service.ProducerService;

@Service
public class ProducerServiceImpl implements ProducerService {
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

	@Override
	public Product addProduct(@Valid ProductDto productDto, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		Product product = this.modelMapper.map(productDto, Product.class);
		product.setUser(account.getUser());
		product.setProducer(account.getUser());
		User user = account.getUser();
		
		product.setStatus("Pending");
		return this.productRepository.save(product);
	}

	@Override
	public ProductDto updateaddedProduct(@Valid ProductDto productDto, String token, Long productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product id", productId.toString()));
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credntial", email));
		product.setCategory(productDto.getCategory());
		product.setImage(productDto.getImage());
		product.setPrice(productDto.getPrice());

		Product updateproduct = this.productRepository.save(product);

		return this.modelMapper.map(updateproduct, ProductDto.class);

	}

	@Override
	public ProductDto addnewProduct(ProductDto productDto, Long storeId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user = account.getUser();
		Store store = this.storeRepository.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store", "store Id", storeId.toString()));
		Product product = this.modelMapper.map(productDto, Product.class);

		product.setUser(user);
		// product.setProducer(user.getProducer());
	//	product.setStores(stores);
		product.setStatus("PENDING");
		Product newProduct = this.productRepository.save(product);
		return this.modelMapper.map(newProduct, ProductDto.class);
	}

	@Override
	public ProductDto getProductById(Long productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId.toString()));

		return this.modelMapper.map(product, ProductDto.class);
	}

	@Override
	public List<ProductDto> getProductByUser(Long userId, String jwtFromRequest) {
		String email = jwtUtils.getUserNameFromToken(jwtFromRequest);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user = account.getUser();
		List<Product> products = this.productRepository.findByUser(user);
		List<ProductDto> productDtos = products.stream()
				.map((product) -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
		return productDtos;
	}


//	
	///it only get product those product are alrady sold
	@Override
	public List<ProductDto> getProductAsStore(String token) {
	    List<Product> products = this.productRepository.findAll();
	    
	    List<ProductDto> productDtos = products.stream()
	            .filter(Product::isRequestSent)
	            .map((product) -> this.modelMapper.map(product, ProductDto.class))
	            .collect(Collectors.toList());
	    
	    return productDtos;
	}



	@Override
	public List<ProductDto> getAllProducts(String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credentials", email));

		List<Product> productList = productRepository.findAll();
		return productList.stream().map((s) -> this.modelMapper.map(s, ProductDto.class)).collect(Collectors.toList());

	}

	@Override
	public Product getProductByProductId(Long productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId.toString()));
		System.out.println("/////////////product/////////////////////////////" + product);
		System.out.println("/////////////productId/////////////////////////////" + productId);
		return product;
	}

	@Override
	public User getUserById(Long producerId, String token) {
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user = account.getUser();
		User userid = userRepository.findById(user.getUser_id())
				.orElseThrow(() -> new ResourceNotFoundException("user", "user Id", producerId.toString()));
		System.out.println("/////////////user/////////////////////////////" + user);
		System.out.println("/////////////userid/////////////////////////////" + userid);
		return userid;
	}

	@Override
	@Transactional
	public boolean sendProductRequest(Long productId, Long producerId, String token) {
		Optional<Product> productOptional = productRepository.findById(productId);
		String email = jwtUtils.getUserNameFromToken(token);
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user", "credential", email));
		User user = account.getUser();
		
		Product product = productOptional.get();
		product.setProducer(account.getUser());
		product.setUser(user);
		System.out.println("/////////////Product for product/////////////////////////////" + product);
	
		product.setRequestSent(true);

	
		productRepository.save(product);

	

		Request productRequest = new Request();
		productRequest.setStatus(RequestStatus.PENDING.name());

		productRequest.setProducer(account.getUser());
		productRequest.setProduct(product);
		productRequest.setStoreManager(user);
		requestRepository.save(productRequest);
		return true;
	}

	@Override
	public List<ProductDto> getPrdetail(String token) {
	List<Product> products = this.productRepository.findAll();
		
		List<ProductDto> productDtos = products.stream()
				.map((product) -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
		return productDtos;
	}

}
