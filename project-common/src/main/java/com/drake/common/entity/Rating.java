package com.drake.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Controller;

import com.drake.common.entity.product.Product;

@Entity
@Table(name="ratings")
public class Rating extends IdBaseEntity{

	@Column(name = "star")
	private double star;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	

	public double getStar() {
		return star;
	}

	public void setStar(double star) {
		this.star = star;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	

	public Rating() {
		super();
	}

	

	public Rating(Product product, Customer customer) {
		super();
		this.product = product;
		this.customer = customer;
	}

	public Rating(Product product) {
		super();
		this.product = product;
	}

	public Rating(Customer customer) {
		super();
		this.customer = customer;
	}
	
	
	
}
