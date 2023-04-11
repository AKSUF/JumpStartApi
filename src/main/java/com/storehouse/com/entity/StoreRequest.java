package com.storehouse.com.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.storehouse.com.dto.StoreManagerDto;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
//will store request from the storemanager 
public class StoreRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long requestId;
	// connect with product table,a id getted by it then when the storemabager click
	// on button it will automatically get yhe product id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	// connect with product table,a id getted by it then when the storemabager click
	// on button it will automatically get yhe product id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_manager_id")
	private StoreManager storeManager;
	private boolean approved;

}
