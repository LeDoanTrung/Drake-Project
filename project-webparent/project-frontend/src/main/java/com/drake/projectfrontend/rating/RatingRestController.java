package com.drake.projectfrontend.rating;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drake.common.entity.Customer;
import com.drake.common.entity.Rating;
import com.drake.common.entity.product.Product;
import com.drake.common.exception.ProductNotFoundException;
import com.drake.projectfrontend.ControllerHelper;
import com.drake.projectfrontend.product.ProductService;

@RestController
public class RatingRestController {

	@Autowired private RatingService ratingService;
	@Autowired private ControllerHelper controllerHelper;
	@Autowired private ProductService productService;
	
	@PostMapping("/rating/{productAlias}/{ratingValue}")
	public String rating(@PathVariable("productAlias") String productAlias, HttpServletRequest request ,Model model, 
			@PathVariable("ratingValue") Integer ratingValue) throws ProductNotFoundException {
			
			Customer customer = controllerHelper.getAuthenticatedCustomer(request);
			
			Product product = productService.getProduct(productAlias);
			Rating ratingNew = new Rating(product, customer);
			product.setShopping(false);
			ratingNew.setStar(ratingValue);
			product.setStar(ratingValue);
				
			ratingService.save(ratingNew);
				
			return "Thank you for your rating!";
			
			
	}
}
