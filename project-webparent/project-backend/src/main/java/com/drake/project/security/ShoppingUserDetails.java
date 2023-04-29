package com.drake.project.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.drake.common.entity.Role;
import com.drake.common.entity.User;




public class ShoppingUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private User user;
	
	public ShoppingUserDetails (User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		
		List<SimpleGrantedAuthority> authories = new ArrayList<>();
		
		for (Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authories;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() { //Ko lock account này
		// TODO Auto-generated method stub
		return true; //true -> account ko bị locked, false -> account bị locked
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true; //true-> ko hết hạn
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled(); //kiểm tra user có enabled hay k -> ko thì ko login
	}
	
	public String getFullName() {
		return this.user.getFirstName()+" " +this.user.getLastName();
	}
	
	public void setFirstName(String firstName) {
		this.user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
	
	public Set<Role> getRoles() {
		Set<Role> roles = user.getRoles();
		return roles;
	}

	public void setPhotos(String fileName) {
		 this.user.setPhotos(fileName);
	}
	
}
