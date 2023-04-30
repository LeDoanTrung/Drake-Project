package com.drake.project.product;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.common.entity.product.Product;
import com.drake.project.brand.BrandService;
import com.drake.project.category.CategoryService;
import com.drake.project.user.export.ProductCsvExporter;



@Controller
public class ProductController {
	@Autowired
	private ProductService service;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private  CategoryService categoryService;
	
	@GetMapping("/products/")
	public String listAll(Model model) {
		return listByPage(1, model, "name", "asc", null, null);
	}
	
	
	@GetMapping("/products/new")
	public String createNew(Model model) {
		List<Brand> listBrands = brandService.listAll(); 
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("product", product);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct (Product product, RedirectAttributes redirectAttributes,
			@RequestParam(value="image", required = false) MultipartFile mainMultipartFile,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@RequestParam(name="detailIDs", required= false) String [] detailIDs,
			@RequestParam(name="detailNames", required= false) String [] detailNames,
			@RequestParam(name="detailValues", required= false) String [] detailValues)throws IOException {
		
//			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Amina Elshal.png--> Amina Elshal.png
//			product.setMainImage(fileName);
			product.setCreateTime(new Date());
			product.setUpdatedTime(new Date());
			
			ProductSaveHelper.setMainImageName(mainMultipartFile, product);
			ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
			ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
			ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
			Product savedProduct = service.save(product);
			
			
//			String uploadDir = "product-images/" + savedProduct.getId(); // tạo folder user-photos theo id để lưu hình
//			
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
			
			ProductSaveHelper.saveUploadedImages(mainMultipartFile, extraImageMultiparts, savedProduct);
			
			ProductSaveHelper.deleteExtraImagesWereRemovedOnForm(product);
		
		
		redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct ( Model model, RedirectAttributes redirectAttributes, @PathVariable(name="id") Integer id) {
		try {
	
			List<Brand> listBrands = brandService.listAll(); 
			Product product = service.get(id);
			Integer numberOfExistingExtraImages = product.getImages().size();
			product.setUpdatedTime(new Date());
			
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id +")");
			
			return "products/product_form";
			
		} catch (ProductNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/products";
		}
		
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteCategory (@PathVariable(name="id") Integer id, Model model,  RedirectAttributes redirectAttributes ) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Product successfully");
			
			return "redirect:/products";
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String enabledCategory(@PathVariable(name="id") Integer id, @PathVariable(name="status") boolean enabled, Model model,RedirectAttributes redirectAttributes) {
		service.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID "+ id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
				
		return "redirect:/products";
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir="asc";
		}
		System.err.println("Sort Field:" + sortField);
		System.err.println("Sort Dir:"+sortDir);
		

		Page<Product> page = service.listByPage(pageNum, sortField, sortDir, keyword, categoryId); 
		List<Product> listProducts = page.getContent();
		
		long startCount = (pageNum-1)*service.PRODUCTS_PER_PAGE+1;
		long endCount = startCount + service.PRODUCTS_PER_PAGE -1;
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "/products/products";
	}
	
	@GetMapping("/products/{pageNum}/{sortDir}/{sortField}/{keyword}/{categoryId}")
	public String listByPageKeyword(@PathVariable(name="pageNum") int pageNum, Model model,
			@PathVariable(name="sortField") String sortField, @PathVariable(name= "sortDir") String sortDir,
			@PathVariable(name="keyword") String keyword,
			@PathVariable(name="categoryId") Integer categoryId) {
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
		
		System.err.println("Sort Field:" + sortField);
		System.err.println("Sort Dir:"+sortDir);
		

		Page<Product> page = service.listByPage(pageNum, sortField, sortDir, keyword, categoryId); 
		List<Product> listProducts = page.getContent();
		
		long startCount = (pageNum-1)*service.PRODUCTS_PER_PAGE+1;
		long endCount = startCount + service.PRODUCTS_PER_PAGE -1;
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "/products/products";
	}
	@GetMapping("/products/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException{
		List<Product> listProducts = service.listAll();
		ProductCsvExporter exporter = new ProductCsvExporter();
		exporter.export(listProducts, response);
	}
	
	
	@GetMapping("/products/detail/{id}")
	public String detailModal(@PathVariable(name ="id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Product product = service.get(id);
			model.addAttribute("product", product);
			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
		
	}
}
