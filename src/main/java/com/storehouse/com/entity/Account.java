package com.storehouse.com.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table
@Getter
@Setter
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long account_id;
@NotNull
@Email
private String email;
private String password;
private String provider;

@OneToMany(mappedBy = "account", cascade = CascadeType.MERGE)
private List<UserRole> userRoles;

@OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)

private User user;

}
