package com.drake.projectfrontend.rating;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Customer;
import com.drake.common.entity.Rating;
import com.drake.common.entity.product.Product;

@Service
@Transactional
public class RatingService {

	@Autowired private RatingRepository ratingRepository;
	
	public void save(Rating rating ) {
		ratingRepository.save(rating);
	}
	
	public Rating listRatingByProduct(Integer productId) {
		return ratingRepository.listStarByProduct(productId);
	}
	
}
