package com.drake.projectfrontend.favorlist;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drake.common.entity.Customer;
import com.drake.projectfrontend.Utility;
import com.drake.projectfrontend.customer.CustomerNotFoundException;
import com.drake.projectfrontend.customer.CustomerService;

@RestController
public class FavorItemRestController {

	@Autowired private FavorItemService favorService;
	@Autowired private CustomerService customerService;
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/favor/add/{productId}")
	public String addProductToFavor(@PathVariable("productId") Integer productId,HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			if (favorService.checkFavorItem(productId, customer)) {
				return "Item has already in your favorite list";
			}
			favorService.addProductFavor(productId, customer);
			return "Item were added to your favorite list.";
			
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to favorite list.";
		} catch (FavorItemException e) {
			return e.getMessage();
		}
	}
	
	@DeleteMapping("/favor/remove/{productId}")
	public String removeProduct(@PathVariable ("productId") Integer productId,
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			favorService.removeProduct(productId, customer);
			
			return "The product has been removed from your favorite list";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
	}
	
}
