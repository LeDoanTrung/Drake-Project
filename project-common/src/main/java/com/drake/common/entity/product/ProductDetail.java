package com.drake.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.drake.common.entity.IdBaseEntity;
import com.drake.common.entity.product.Product;

@Entity
@Table(name = "product_details")
public class ProductDetail extends IdBaseEntity{
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(nullable = false, length = 255)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductDetail(String name, String value, Product product) {
		super();
		this.name = name;
		this.value = value;
		this.product = product;
	}

	public ProductDetail(Integer id, String name, String value, Product product) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ProductDetail(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public ProductDetail() {
		super();
	}

	
}
