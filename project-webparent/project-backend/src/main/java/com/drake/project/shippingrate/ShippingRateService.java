package com.drake.project.shippingrate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drake.common.entity.product.Product;
import com.drake.project.product.ProductRepository;



@Service
@Transactional
public class ShippingRateService {

	private static final int DIM_DIVISOR = 139;	
	@Autowired private ProductRepository productRepo;
	
	public float calculateShippingCost(Integer productId) {
		Product product = productRepo.findById(productId).get();
		
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		float shippingCost = finalWeight * 1;
		
		return shippingCost;
	}
}
