package com.drake.projectfrontend.shoppingcart;

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
public class ShoppingCartRestController {

	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
	
	@PostMapping("/cart/add/{productId}/{quantity}/{size}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity,@PathVariable("size") Integer size ,HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity,size, customer);
			
			return updatedQuantity + " items(s) of this product were added to your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart.";
		} catch (ShoppingCartException e) {
			return e.getMessage();
		}
	}
	
	
	
	@PostMapping("/cart/update/{productId}/{quantity}/{size}")
	public String updateQuantity(@PathVariable("productId") Integer productId, 
			@PathVariable("quantity") Integer quantity, @PathVariable("size") Integer size,HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subTotal =cartService.updateQuantity(productId, quantity, size,customer);
		
			return String.valueOf(subTotal);
		} catch (CustomerNotFoundException e) {
			return "You must login to change quantity of product";
		}
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable ("productId") Integer productId,
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			
			return "The product has been removed from your shopping cart";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
	}
}
