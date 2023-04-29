package com.drake.projectfrontend.brand;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.drake.common.entity.Brand;


public interface BrandRepository extends CrudRepository<Brand, Integer>{

	@Query("SELECT b FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAllEnabled();
	
	@Query("SELECT b FROM Brand b WHERE b.name = ?1")
	public Brand findByNameEnabled(String name);
}
