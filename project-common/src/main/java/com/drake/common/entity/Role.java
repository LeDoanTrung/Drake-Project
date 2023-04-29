package com.drake.common.entity;
// Tạo entity Role tương ứng với table roles

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//com.model Vehicle
//com.entity Vehicle => ko cần tạo table Vehicle trong DB vì nó sẽ khởi tạo giùm mình


@Entity // --> chú thích đây là một Entity và tạo table. Nếu ko có sẽ ko tạo table
@Table(name ="roles")
public class Role extends IdBaseEntity{
	// column name chứa tối đa 40 ký tự , ko chứa null, ko chứa các giá trị trùng lặp
	@Column(length = 40, nullable =false, unique = true)
	private String name;
	
	@Column(length = 150, nullable =false)
	private String description;

	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public Role(String name) {
		super();
		this.name = name;
	}
	
//	@ManyToOne
//	private User user123;
	
	
	public Role() {
		
	}
	
	public Role(Integer id) {
		this.id =id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override 
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null)? 0 : id.hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals (Object obj) {
		if(this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		Role other = (Role) obj;
		if(id == null) {
			if(other.id != null)
				return false;
		} else if(!id.equals(other.id))
			return false;
		
		return true;
	}
	
	// thay vì hiển thị đối tượng role thì hiển thị name trên UI
	@Override
	
	public String toString() {
		return this.name;
	}
	
	
}
