package com.storehouse.com.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Customer {
	@Id

	private Long customerId;
	@ManyToMany
	@JoinColumn(name="cusstoreId",referencedColumnName = "storeId")
	private List<Store>stores;
	@OneToOne(mappedBy = "customer")
	private Cart cart;
	
	@OneToOne
	 @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	@JsonIgnore
	private User user;

	
}
