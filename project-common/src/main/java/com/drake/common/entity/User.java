package com.drake.common.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

// Tạo entity "User" tương ứng với table users
// Nếu ko khai báo name của @Entity hoặc @Table thì nó lấy tên class đặt làm tên của @Entity và @Table
@Entity
@Table(name = "users")
public class User extends IdBaseEntity{
	@Column(length = 128, nullable =false, unique = true)
	private String email;
	
	@Column(length = 64, nullable =false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	
	@Column(length = 64)
	private String photos; // để lưu tên image
	
	
	private boolean enabled;
	
	// mối quan hệ giữa users và roles là ManyToMany --> tạo ra table thứ 3 tên là
	// user_roles
	// column user id sẽ trỏ tới column id của table users
	// column role_id sẽ trỏ tới column id của table roles
	
	@ManyToMany(fetch = FetchType.EAGER)
	//Lazy - nó sẽ ko get roles tương ứng của user đó -> nếu muốn get thì phải dùng phương thức user.getRole
	// EAGER - khi get user nó sẽ get tất cả roles của user đó
	@JoinTable(name = "users_roles", 
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	public User() {
		
	}

	//1 user có nhiều Role 
//	@OneToMany(mappedBy = "user123")
//	private Set<Role> children = new HashSet<>();
	
	
	
	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	//thêm role mới vafp thuộc tính roles của user
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	@Transient //trả về fullname khi màn hình nhỏ
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	@Transient
	public String getPhotosImagePath() {
		if (id == null || photos == null ) return "/images/default-user.png"; //nếu ko có image thì hiển thị
		
		return "/user-photos/" + this.id + "/"+ this.photos;
	}
	
	
	// @Override phương thức toString() để dùng khi System.out.println(user);
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();
		
		while (iterator.hasNext()) {
			Role role = (Role) iterator.next();
			
			if (role.getName().equals(roleName)) {
				return true;
			}
			
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "User [id="+ id + ", email=" + email + ", firstName=" + firstName + ", lastName=" +lastName
				+", roles=" + roles +"]";
	}
	
	
}
