package com.storehouse.com.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class StoreProduct {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeproductId;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "store_store_id")
private Store store;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "product_product_id")
private Product product;

private int quantity;

@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "requestId")
private Request request;

	
}
