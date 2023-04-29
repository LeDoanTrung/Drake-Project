package com.drake.common.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity
@Table(name="brands")
public class Brand extends IdBaseEntity{
	
	@Column(length = 128, nullable = false)
	private String logos;

	@Column(length = 45, nullable = false, unique = true)
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogos() {
		return logos;
	}

	public void setLogos(String logos) {
		this.logos = logos;
	}
	
	//Nếu ko có table brands_categories thì trên view sẽ ko hiện vì ko ánh xạ
	// Chỉ tạo joinTable khi có mối quan hệ Many to Many
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "brands_categories", 
	joinColumns = @JoinColumn(name = "brand_id"),
	inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	
	
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Brand() {
		
	}

	public Brand(String name, String logos, Set<Category> categories) {
		super();
		this.name = name;
		this.logos = logos;
		this.categories = categories;
	}
	
	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	@Transient
	public String getPhotosImagePath() {
		if (id == null || logos == null ) return "/images/image-thumbnail.png"; //nếu ko có image thì hiển thị
		
		return "/brand-logos/" + this.id + "/"+ this.logos;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
