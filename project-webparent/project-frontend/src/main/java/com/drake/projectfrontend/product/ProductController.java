package com.drake.projectfrontend.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.common.entity.Rating;
import com.drake.common.entity.product.Product;
import com.drake.common.exception.BrandNotFoundException;
import com.drake.common.exception.CategoryNotFoundException;
import com.drake.common.exception.ProductNotFoundException;
import com.drake.projectfrontend.brand.BrandService;
import com.drake.projectfrontend.category.CategoryService;
import com.drake.projectfrontend.rating.RatingService;

@Controller
public class ProductController {

	@Autowired private ProductService productService;
	
	@Autowired private CategoryService categoryService;
	
	@Autowired private BrandService brandService;
	
	@Autowired private RatingService ratingService;
	
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
			Model model) {
		return viewCategoryByPage(alias, 1, model);
	}
	
	@GetMapping("/b/{brand_name}")
	public String viewBrandFirstPage(@PathVariable("brand_name") String name,
			Model model) {
		return viewBrandByName(name, 1, model);
	}
	
	@GetMapping("/b/{brand_name}/page/{pageNum}")
	public String viewBrandByName(@PathVariable("brand_name") String name,
			@PathVariable("pageNum") int pageNum,
			Model model) {
		Brand brand = brandService.getBrand(name);		
		//List<Category> listCategoryParents = categoryService.getCategoryParents(category);//lấy ra tất cả categories cha,ông,...của category hiện tại
		
		Page<Product> pageProducts = productService.listByBrand(pageNum, brand.getId());//lấy ra tất cả products thuộc về category hiện tại và tất cả products thuộc về categories con,cháu,... của category hiện tại
		List<Product> listProducts = pageProducts.getContent();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", brand.getName());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("brand", brand);
		
		return "product/products_by_brand";
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") int pageNum,
			Model model) {
		try {
			Category category = categoryService.getCategory(alias);		
			//List<Category> listCategoryParents = categoryService.getCategoryParents(category);//lấy ra tất cả categories cha,ông,...của category hiện tại
			
			Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());//lấy ra tất cả products thuộc về category hiện tại và tất cả products thuộc về categories con,cháu,... của category hiện tại
			List<Product> listProducts = pageProducts.getContent();
			
			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}
			
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("pageTitle", category.getName());
			//model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("category", category);
			
			return "product/products_by_category";
		} catch (CategoryNotFoundException ex) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{product_alias}/")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model,
			HttpServletRequest request) {
		try {
			Product product = productService.getProduct(alias);
			
			Category category = categoryService.getCategory(product.getCategory().getAlias());
			
			
			model.addAttribute("category", category);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());
			
			return "product/product_detail";
		} catch (Exception e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String searchFirstPage(String keyword, Model model) {
		return searchByPage(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(String keyword, @PathVariable("pageNum") int pageNum, Model model) {
		Page<Product> pageProducts = productService.search(keyword, pageNum);//dùng FULL TEXT SEARCH trong SQL
		List<Product> listResult = pageProducts.getContent();//khi bấm search thì chỉ cần lấy ra tất cả products có name,short_description,full_description trùng với keyword, ko lấy ra categories
		
		long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", keyword + " - Search Result");
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("searchKeyword", keyword);
		model.addAttribute("listResult", listResult);
		
		return "product/search_result";
	}		
}
