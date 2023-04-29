package com.drake.project.product;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.drake.common.entity.product.Product;



@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository proRepo;

	public static final int PRODUCTS_PER_PAGE = 5;
	
	public List<Product> listAll() {
		return (List<Product>) proRepo.findAll(Sort.by("name").ascending());
	}
	
	
	public Product save (Product product) {
		return proRepo.save(product);
	}
	
	public Product get(Integer Id) throws ProductNotFoundException{
		try {
			return this.proRepo.findById(Id).get();
		}
		catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID "+ Id);
		}
	}
	
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		proRepo.updateEnabledStatus(enabled, id);
	}
	
	public void delete(Integer Id) throws ProductNotFoundException{
		Long countById = proRepo.countById(Id);
		if(countById == null|| countById==0) {
			throw new ProductNotFoundException("Could not find any product with ID "+ Id);
		}
		this.proRepo.deleteById(Id);
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		   boolean isCreatingNew = (id == null|| id==0);
		   
		   Product productByName = proRepo.getProductByName(name);
		   
		   if (isCreatingNew) {
			   if(productByName != null) {
			return "DuplicateName";
		   } else {
		   Product productByAlias = proRepo.findByAlias(alias);
			if (productByAlias != null) {
				return "DuplicateAlias";
			}
		   }
	   } else { // Đây là edit, đã có ID, trong trường hợp mà thg Cate có name trùng và cùng ID thì nó đang kiểm tra với chính nó
		   if (productByName != null && productByName.getId() != id) {
			   return "DuplicateName";
		   }
		   
		   Product productByAlias = proRepo.findByAlias(alias);
		   if (productByAlias != null && productByAlias.getId() != id) {
			   return "DuplicateAlias";
		}
	   	}
		   
		   return "OK";
	   }
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword,
			Integer categoryId) {
		
		Sort sort = Sort.by(sortField); // sortField phải giống vs thuộc tính bên Entity
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE,sort);
		
		Page<Product> page = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId)+"-";
				page = proRepo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			} else {
				page = proRepo.findAll(keyword, pageable);
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId)+"-";
				page = proRepo.findAllInCategory(categoryId, categoryIdMatch, pageable);
			} else {
				page = proRepo.findAll(pageable);
			}
		}
		return page;
	}
	
	
	public Product getProductByName(String name) throws ProductNotFoundException{
		try {
			return this.proRepo.getProductByName(name);
		}
		catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with Name "+ name);
		}
	}
	
	public Page<Product> searchProducts(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		return proRepo.searchProductsByName(keyword, pageable);		
	}
}
