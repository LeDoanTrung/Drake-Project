package com.drake.project.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.drake.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

	@Query("SELECT u FROM Product u WHERE u.name = :name")
	public Product getProductByName(@Param("name")String name);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% ")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);

	public Product findByName(String name); 
	
	public Product findByAlias(String alias);
	
	@Query("update Product u set u.enabled = ?1 where u.id = ?2") 
	@Modifying 
	public void updateEnabledStatus(boolean enabled, Integer Id);
	
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%) AND "
			+ "(p.name LIKE %?3% " 
			+ "OR p.shortDescription LIKE %?3% "
			+ "OR p.fullDescription LIKE %?3% "
			+ "OR p.brand.name LIKE %?3% "
			+ "OR p.category.name LIKE %?3%)")			
	public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, 
			String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%")	
	public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, 
			Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductsByName(String keyword, Pageable pageable);
}
