package com.drake.projectfrontend.favorlist;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Customer;
import com.drake.common.entity.FavorItem;
import com.drake.common.entity.product.Product;
import com.drake.projectfrontend.product.ProductRepository;

@Service
@Transactional
public class FavorItemService {

	@Autowired FavorItemRepository favorRepo;
	
	@Autowired ProductRepository productRepo;
	
	
	public void addProductFavor(Integer productId, Customer customer) 
			throws FavorItemException{
	
		Product product = new Product(productId);
	
		FavorItem favorItem = favorRepo.findByCustomerAndProduct(customer, product);
	
	
		favorItem = new FavorItem();
		favorItem.setCustomer(customer);
		favorItem.setProduct(product);
	
		favorRepo.save(favorItem);
	
	}
	
	public List<FavorItem> listFavorItems(Customer customer) {
		return favorRepo.findByCustomer(customer);
	}
	
	public boolean checkFavorItem(Integer productId, Customer customer) throws FavorItemException{
		
		Product product = new Product(productId);
		
		FavorItem favorItem = favorRepo.findByCustomerAndProduct(customer, product);
		if (favorItem == null ) {
			return false;
		}
		return true;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		favorRepo.deleteByCustomerAndProduct(customer.getId(), productId);//xóa product trong cartItem
	}
	
	public void deleteByCustomer(Customer customer) {
		favorRepo.deleteByCustomer(customer.getId());
	}
}
