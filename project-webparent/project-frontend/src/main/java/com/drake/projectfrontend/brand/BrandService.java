package com.drake.projectfrontend.brand;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Brand;

@Service
public class BrandService {

	@Autowired BrandRepository repo;
	
	public List<Brand> listAllBrands(){
		List<Brand> listBrands = repo.findAllEnabled();
		return listBrands;
	}
	
	public Brand getBrand(String name) {
		Brand brand = repo.findByNameEnabled(name);
		return brand;
	}
}
