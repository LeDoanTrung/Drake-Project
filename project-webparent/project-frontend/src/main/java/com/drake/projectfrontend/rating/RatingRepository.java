package com.drake.projectfrontend.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.drake.common.entity.Rating;
import com.drake.common.entity.product.Product;

public interface RatingRepository extends JpaRepository<Rating, Integer>{

	@Query("SELECT r FROM Rating r WHERE r.product.id = ?1")
	public Rating listStarByProduct(Integer productId);
	
	@Query("SELECT r FROM Rating r WHERE r.customer.id = ?1")
	public Rating listStarByCustomer(Integer customerId);
	
	
}
