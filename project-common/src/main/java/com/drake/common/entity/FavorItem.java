package com.drake.common.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.drake.common.entity.product.Product;

@Entity
@Table(name = "favor_items")
public class FavorItem extends IdBaseEntity{
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

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
	
	public FavorItem() {
		super();
	}
	
	@Override
	public String toString() {
		return "FavorItem [id=" + id + ", customer=" + customer.getFullName() + ", product=" + product.getShortName() +"]";
	}
}
