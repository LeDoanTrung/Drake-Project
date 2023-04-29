package com.drake.common.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.drake.common.entity.product.Product;



@Entity
@Table(name = "cart_items")
public class CartItem extends IdBaseEntity{
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	private int quantity;
	
	private int size;
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Transient
	private float  shippingCost;

	public CartItem() {
		super();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	@Override
	public String toString() {
		return "CartItem [id=" + id + ", customer=" + customer.getFullName() + ", product=" + product.getShortName() + ", quantity=" + quantity+ "]";
	}
	
	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice()*	quantity;
	}
	
	@Transient
	public float getShippingCost() {
		return shippingCost;
	}
	
	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
}
