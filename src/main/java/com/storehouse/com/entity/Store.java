package com.storehouse.com.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter

public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long storeId;
	private String storeName;
	private String storeLocation;
	private String catagory;
	private String openday;
	private String storeImage;
	private String storedesc;
	@ManyToMany(mappedBy = "stores")
	private List<Customer> customers;

	@OneToMany(mappedBy = "store")
	private List<DeliveryMan> deliveryMans;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storemanagerId", referencedColumnName = "user_id")
	private User storeManager;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "requestId", referencedColumnName = "requestId")
	private Request request;

	@ManyToOne
	private Admin admin;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreProduct> storeProducts = new ArrayList<>();

}
