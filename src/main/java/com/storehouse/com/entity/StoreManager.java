package com.storehouse.com.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
public class StoreManager {
	@Id

	private Long storemanagerId;
	@OneToOne
	 @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private User userstoremanager;
	@OneToOne
	@JoinColumn(name = "storeId")
	private Store storemanager;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "storeManager")
	private List<StoreRequest>storeRequests;
	
	
	

}
