package com.drake.common.entity;

public class CartItemDTO {
	private Integer id;
	private String name;
	public CartItemDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public CartItemDTO() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
