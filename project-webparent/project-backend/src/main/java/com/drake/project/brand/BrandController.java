package com.drake.project.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.project.FileUploadUtil;
import com.drake.project.category.CategoryService;
import com.drake.project.user.export.BrandCsvExporter;

@Controller
public class BrandController {

	@Autowired 
	private BrandService service;
	
	@Autowired
	private CategoryService cateService;
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/brands/new")
	public String createNew (Model model) {
		List<Category> listCategories = cateService.listCategoriesUsedInForm();
		model.addAttribute("listCategories", listCategories);
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		model.addAttribute("pageTitle","Create New Brand");
		return "brands/brands_form";
		
	}
	
	@PostMapping("/brands/save")
	public String saveBrand (Brand brand, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Amina Elshal.png--> Amina Elshal.png
			brand.setLogos(fileName);
			Brand savedBrand = service.save(brand);
			
			String uploadDir = "../brand-logos/" + savedBrand.getId(); // tạo folder user-photos theo id để lưu hình
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
		}
		else {
			if (brand.getLogos().isEmpty()) brand.setLogos(null); { // nếu ko chọn image thì lưu null
				service.save(brand);
			}
		}
	
		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
		
		
		return "redirect:/brands";
		
		//redirect sẽ trả về một thằng getMapping. Nếu chỉ để return "redirect:/"; 
		//thì nó trả về trang mặc định
	}
	
	private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc" ;
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name= "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = service.get(id);
			List<Category> listCategories = cateService.listCategoriesUsedInForm();
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle","Edit Brand (ID: " +id+")");
			
			return "brands/brands_form";
		}
		catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return defaultRedirectURL ;
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name= "id") Integer id,Model model, RedirectAttributes redirectAttributes) {
		try {
		service.delete(id);
		String brandPhotoDir ="../brand-logos/" + id;
		FileUploadUtil.removeDir(brandPhotoDir);
		redirectAttributes.addFlashAttribute("message","Delete brand successfully");
		
		}
		catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			
		}
		return defaultRedirectURL ;
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortFiled") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		System.out.println("Sort Field: "+sortField);
		System.out.println("Sort Order: "+sortDir);
		
		Page<Brand> page = service.listByPage(pageNum, sortField, sortDir, keyword); 
		List<Brand> listBrands = page.getContent();
		
		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1; //1  5
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1; //4   6
		
		if (endCount > page.getTotalElements()) { //trường hợp endCount = 21 + 3 = 24 > 23 --> set lại endCount = 23
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "brands/brands";
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = service.listAll();
		BrandCsvExporter exporter = new BrandCsvExporter();
		exporter.export(listBrands, response);
	}
	
	
}
