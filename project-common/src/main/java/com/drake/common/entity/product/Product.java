package com.drake.common.entity.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.common.entity.IdBaseEntity;
import com.drake.common.entity.Rating;
import com.drake.common.entity.product.ProductDetail;
import com.drake.common.entity.product.ProductImage;


@Entity
@Table(name = "products")
public class Product extends IdBaseEntity{
	@Column(length = 255, nullable = false, unique = true)
	private String name;
	
	@Column(length = 255, nullable = false, unique = true)
	private String alias;
	
	@Column(length = 512, nullable = false, name = "short_description")
	private String shortDescription;
	
	@Column(length = 4096, nullable =false, name = "full_description")
	private String fullDescription;
	
	@Column(nullable = false, name = "main_image")
	private String mainImage;
	
	@Column(nullable = false, name = "create_time", updatable = false)
	private Date createTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	private boolean enabled;
	
	@Column(name = "in_stock")
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	@Column(name = "discount_percent")
	private float discountPercent;
	
	private float length;
	
	private float width;
	
	private float height;
	
	private float weight;
	
	private double star;
	
	public double getStar() {
		return star;
	}

	public void setStar(double star) {
		this.star = star;
	}

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetail> details = new ArrayList<>();
	
	private boolean isShopping;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings = new ArrayList<>();
	
	public boolean isShopping() {
		return isShopping;
	}

	public void setShopping(boolean isShopping) {
		this.isShopping = isShopping;
	}

	public Product (Integer id) {
		this.id =id;
	}
	
	public Product (String name) {
		this.name =name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	
	
	public Product() {
		super();
	}
	


	public Product(String name, String alias, String shortDescription, String fullDescription, String mainImage,
			Date createTime, Date updatedTime, boolean enabled, boolean inStock, float cost, float price,
			float discountPercent, float length, float width, float height, float weight, Brand brand,
			Category category) {
		super();
		this.name = name;
		this.alias = alias;
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		this.mainImage = mainImage;
		this.createTime = createTime;
		this.updatedTime = updatedTime;
		this.enabled = enabled;
		this.inStock = inStock;
		this.cost = cost;
		this.price = price;
		this.discountPercent = discountPercent;
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;
		this.brand = brand;
		this.category = category;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Transient
	public String getPhotosImagePath() {
		if (id == null || mainImage == null ) return "/images/image-thumbnail.png"; //nếu ko có image thì hiển thị
		
		return "/product-images/" + this.id + "/"+ this.mainImage;
	}
	
	
	public void addDetail(String name, String value) {
		this.details.add(new ProductDetail(name, value, this));
	}
	
	public void addDetail(Integer id, String name, String value) {
		this.details.add(new ProductDetail(id, name, value, this));
	}

	

	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}

	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}
	
	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}
	
	public boolean containsImageName(String imageName) {
		Iterator<ProductImage> iterator = images.iterator();
		
		while (iterator.hasNext()) {
			ProductImage image = iterator.next();
			if (image.getName().equals(imageName)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transient
	public String getShortName() {//hiển thị trên list categories tên ngắn lại(70 ký tự)
		if (name.length() > 70) {
			return name.substring(0, 70).concat("...");
		}
		return name;
	}
	
	@Transient
	public float getDiscountPrice() {
		if (discountPercent > 0) {
			return price * ((100 - discountPercent) / 100);
		}
		return this.price;
	}
	
	@Transient
	public String getURI() {
		return "/p/" + this.alias + "/";
	}
	
	@Transient
	public double getAverageStar() {
		double average = 0.0;
		int count=0;
		for (Rating rating : ratings) {
			if (rating.getProduct().getId() == this.getId()) {
				average += rating.getStar();
				count+=1;
			}
		}
		average = average/count;
		return average;
	}
	
}
