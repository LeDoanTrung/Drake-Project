package com.drake.project.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Brand;
import com.drake.common.entity.Category;
import com.drake.project.category.CategoryRepository;


@Service
@Transactional
public class BrandService {

	@Autowired private BrandRepository brandRepo;
	
	@Autowired private CategoryRepository cateRepo;
	
	public static final int BRANDS_PER_PAGE = 10;
	
	public List<Brand> listAll() {
		return (List<Brand>) brandRepo.findAll(Sort.by("name").ascending());
	}
	
	public List<Category> listCategories() {
		return (List<Category>) cateRepo.findAll(); 
	}
	
	public Brand save (Brand brand) {
		
		return brandRepo.save(brand);
	}
	
	public Brand get(Integer Id) throws BrandNotFoundException{
		try {
			return this.brandRepo.findById(Id).get();
		}
		catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with ID "+ Id);
		}
	}
	
	public void delete(Integer Id) throws BrandNotFoundException{
		Long countById = brandRepo.countById(Id);
		if(countById == null|| countById==0) {
			throw new BrandNotFoundException("Could not find any brand with ID "+ Id);
		}
		this.brandRepo.deleteById(Id);
	}
	
	public boolean isNameUnique(Integer id, String name) {
		Brand brandByName = brandRepo.findByName(name);
		
		if(brandByName == null) return true; //ko trùng email
		
		boolean isCreatingNew = (id == null); //phan biet create va edit, create co id la new user
		
		if(isCreatingNew) {
			if (brandByName != null ) return false;
		}
		else {
			if (brandByName.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort = Sort.by(sortField); // sortField phải giống vs thuộc tính bên Entity
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, BRANDS_PER_PAGE,sort);
		
		if (keyword != null) {
			return brandRepo.findAll(keyword, pageable);
		}
		return brandRepo.findAll(pageable);
	}
	
	
	public Brand getBrandByName(String name) throws BrandNotFoundException{
		try {
			return this.brandRepo.getBrandByName(name);
		}
		catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with Name "+ name);
		}
	}
}
