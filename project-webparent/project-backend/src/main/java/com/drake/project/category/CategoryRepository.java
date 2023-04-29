package com.drake.project.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.drake.common.entity.Category;



public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>{

	@Query("SELECT u FROM Category u WHERE u.name = :name")
	public Category getCategoryByName(@Param("name")String name);
	
	public Long countById(Integer id);
	
	
	@Query("update Category u set u.enabled = ?1 where u.id = ?2") // truyền param theo thứ tự index
	@Modifying // Bắt buộc phải có khi dùng UPDATE/ INSERT/ DELETE
	public void updateEnabledStatus(boolean enabled, Integer Id);

	
	@Query("SELECT u FROM Category u WHERE u.name LIKE %?1% OR u.alias LIKE %?1%" )
	public Page<Category> findAll(String keyword, Pageable pageable);

	@Query("SELECT u FROM Category u WHERE u.parent.id is NULL") // parent_id
	public List<Category> findRootCategories(Sort sort);

	@Query("SELECT u FROM Category u WHERE u.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageable);
	
	@Query("SELECT u FROM Category u WHERE u.name LIKE %?1%")
	public Page<Category> search(String keyword, Pageable pageable);
	
	
	public Category findByName(String name); 
		
	public Category findByAlias(String alias);
}
