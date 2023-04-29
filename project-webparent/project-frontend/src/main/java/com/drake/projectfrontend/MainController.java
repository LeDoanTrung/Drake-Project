package com.drake.projectfrontend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.projectfrontend.brand.BrandRepository;
import com.drake.projectfrontend.brand.BrandService;
import com.drake.projectfrontend.category.CategoryService;


@Controller
public class MainController {

	@Autowired private CategoryService categoryService;
	
	@Autowired private BrandService brandService;
	
	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> listCategories = categoryService.listNoChildrenCategories();
		List<Brand> listBrands = brandService.listAllBrands();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listBrands", listBrands);
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}	
}
