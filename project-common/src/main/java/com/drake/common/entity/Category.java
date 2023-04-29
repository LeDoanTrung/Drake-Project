package com.drake.common.entity;



import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;



@Entity
@Table(name="categories")
public class Category extends IdBaseEntity{
	@Column(length = 128, nullable =  false, unique = true)
	private String name;
	
	@Column(length = 64, nullable = false)
	private String alias;
	
	@Column(length = 128, nullable =  false)
	private String image;
	
	
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	
	public String getAllParentIDs() {
		return allParentIDs;
	} 

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();
	
	
	
	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}
	
	public Category (String name, Category parent) {
		this(name);
		this.parent= parent;
	}
	public Category getParent() {
		return parent;
	}
	
    public String getParentName() {
		return this.parent.getName();
	}
    
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	
	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	private boolean enabled;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Category() {
		super();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Transient
	public String getPhotosImagePath() {
		if (id == null || image == null ) return "/images/image-thumbnail.png"; //nếu ko có image thì hiển thị
		
		return "/categories-images/" + this.id + "/"+ this.image;
	}

	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		return copyCategory;
	}
	
	public static Category copyIdAndName(Integer id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);
		return copyCategory;
	}

	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setImage(category.getImage());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size() > 0);
		
		return copyCategory;
	}
	
	
	
	@Transient
	private boolean hasChildren;
	

	public boolean isHasChildren() {
		return hasChildren;
	}

	
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public static Category copyFull(Category category, String name) {
		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(name);
		return copyCategory;
	}
	

	  
}
