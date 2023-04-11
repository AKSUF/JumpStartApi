package com.storehouse.com.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.storehouse.com.status.ProductStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter

public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long productId;
	private String productName;
	private Double price;
	private String image;
	private String status;

	private String productdesc;
	private String category;

	@NotNull
	@Min(value = 0, message = "Stock quantity must not be less than zero")
	private int stockQuantity;

	private boolean approvedByProducer;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "producerId", referencedColumnName = "user_id")
	private User producer;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

	private List<Delivery> deliveries = new ArrayList<>();

	@ManyToMany
	private List<Store> stores;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreProduct> storeProducts = new ArrayList<>();

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "product")
	private List<Delivery> delivery;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_manager_id")
	private User storeManager;

	@Enumerated(EnumType.STRING)
	private ProductStatus productstatus = ProductStatus.PENDING;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<>();
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<CartItem> cartItems;
	private boolean requestSent;
	private boolean paymentcomplete;
	private boolean orderPlaced;

	public boolean isRequested() {
		return this.requestSent;
	}

	public boolean isOrdered() {
		return this.orderPlaced;
	}

}
