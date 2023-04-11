package com.storehouse.com.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class SalesDto {
	private Long saleId;
	private int quantity;
	private Date date;
	private ProductDto product;
	private StoreDto store;
	private CustomerDto customer;
}
