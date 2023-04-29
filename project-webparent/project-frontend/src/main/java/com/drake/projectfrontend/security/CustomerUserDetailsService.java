package com.drake.projectfrontend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.drake.common.entity.Customer;
import com.drake.projectfrontend.customer.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	private CustomerRepository repository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repository.findByEmail(email);
		if (customer == null) {
			throw new UsernameNotFoundException("No customer found with the email: "+email);
		}
		return new CustomerUserDetails(customer);
	}

}
