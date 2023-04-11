package com.storehouse.com.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter

public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long cartitemId;
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	private int quantity;
	@ManyToOne
	@JoinColumn(name="cartId")
	private Cart cart;
	
}
