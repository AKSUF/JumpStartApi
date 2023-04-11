package com.storehouse.com.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table
@Getter
@Setter
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private long cartId;
@OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
private List<CartItem>cartItems;
@OneToOne
@JoinColumn(name = "customerId")
private Customer customer;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "user_id")
private User user;



public void addCartItem(CartItem cartItem) {
    cartItems.add(cartItem);
    cartItem.setCart(this);
}

public void removeCartItem(CartItem cartItem) {
    cartItems.remove(cartItem);
    cartItem.setCart(null);
}

}
