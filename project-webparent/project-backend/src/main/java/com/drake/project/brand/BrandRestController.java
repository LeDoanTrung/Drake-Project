package com.drake.project.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.project.category.CategoryDTO;



@RestController
public class BrandRestController {

	
	@Autowired
	private BrandService service;
	
	@PostMapping("/brands/check_name")
	public String checkDuplicate(Integer id, String name) {
		return service.isNameUnique(id, name) ? "OK" : "Duplicated";	
	}
	
	@GetMapping("/brands/{id}/category")
	public List<CategoryDTO> listCateByBrand(@PathVariable(name="id") Integer id) throws BrandNotFoundException{
		Brand brand = service.get(id);
		Set<Category>setCategories = brand.getCategories();
		List<CategoryDTO> listCategories = new ArrayList();
		for (Category category : setCategories) {
			CategoryDTO dto =  new CategoryDTO(category.getId(), category.getName());
			
			listCategories.add(dto);
			
		}
		return listCategories;
	}
	
}
