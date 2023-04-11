package com.storehouse.com.entity;

import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
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

public class DeliveryMan {
	@Id
	private Long deliverymanId;
	@OneToOne

	 @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private User userdelivryman;
	@ManyToOne
	@JoinColumn(name="storeId")
	private Store store;
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "deliverId", referencedColumnName = "deliverId")
	private Delivery delivery;
	

	
}
