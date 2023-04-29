package com.drake.project.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.drake.common.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{

	@Query("SELECT u FROM Brand u WHERE u.name = :name")
	public Brand getBrandByName(@Param("name")String name); 
	
	@Query("SELECT u FROM Brand u Where CONCAT(u.id,' ',u.name) LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);
	

    public Long countById(Integer id);

    public Brand findByName(String name); 
	
}
