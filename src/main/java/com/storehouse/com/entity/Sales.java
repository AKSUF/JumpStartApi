package com.storehouse.com.entity;

import java.time.LocalDate;
import java.util.Date;

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

public class Sales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long saleId;
private int quantity;
private Date date;
@ManyToOne
@JoinColumn(name="productId")
private Product product;
@ManyToOne
@JoinColumn(name="storeId")
private Store store;
@ManyToOne
private Customer customer;
}
