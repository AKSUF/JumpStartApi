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

public class CustomerStore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long customerstoreId;
@ManyToOne
@JoinColumn(name="customerId")
private Customer customer;
@ManyToOne
@JoinColumn(name="storeId")
private Store store; 
}
