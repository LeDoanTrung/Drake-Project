package com.drake.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.drake.common.entity.User;
import com.drake.project.user.UserRepository;


@Service
public class ShoppingUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 User user = userRepo.getUserByEmail(email);
		 
		 if (user != null) {
			return new ShoppingUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email: "+ email);
	}
}
