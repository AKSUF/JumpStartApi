package com.storehouse.com.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class DeliveryAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryassignId;

	@ManyToOne
	@JoinColumn(name = "deliverId")
	private Delivery delivery;

	
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "deliverymanId", referencedColumnName = "user_id")
	private User deliveryman;


	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "assignedBy", referencedColumnName = "user_id")
	private User assignedBy;

}
