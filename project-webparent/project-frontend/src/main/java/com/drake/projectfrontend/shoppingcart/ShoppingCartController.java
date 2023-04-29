package com.drake.projectfrontend.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.drake.common.entity.CartItem;
import com.drake.common.entity.Customer;
import com.drake.projectfrontend.ControllerHelper;

;

@Controller
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ControllerHelper controllerHelper;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		
		float estimatedTotal = 0.0F;
		
		for (CartItem cartItem : cartItems) {
			estimatedTotal += cartItem.getSubtotal();
		}
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		return "cart/shopping_cart";
	}
	
	
}
