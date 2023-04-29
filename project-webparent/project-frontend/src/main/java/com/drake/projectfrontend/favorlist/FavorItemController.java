package com.drake.projectfrontend.favorlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.drake.common.entity.Customer;
import com.drake.common.entity.FavorItem;
import com.drake.projectfrontend.ControllerHelper;

@Controller
public class FavorItemController {

	@Autowired FavorItemService favorItemService;
	
	@Autowired ControllerHelper controllerHelper;
	
	@GetMapping("/favorite")
	public String viewFavor(Model model, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		List<FavorItem> favorItems = favorItemService.listFavorItems(customer);
		
		
		
		model.addAttribute("favorItems", favorItems);
		
		
		return "cart/favorite";
	}
}
