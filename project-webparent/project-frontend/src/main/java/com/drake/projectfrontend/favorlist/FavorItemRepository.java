package com.drake.projectfrontend.favorlist;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.drake.common.entity.Customer;
import com.drake.common.entity.FavorItem;
import com.drake.common.entity.product.Product;

public interface FavorItemRepository extends CrudRepository<FavorItem, Integer>{

	public List<FavorItem> findByCustomer (Customer customer);
	
	public FavorItem findByCustomerAndProduct(Customer customer, Product product);

	
	@Modifying
	@Query("DELETE FROM FavorItem f WHERE f.customer.id = ?1 AND f.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
	
	@Modifying
	@Query("DELETE FavorItem f WHERE f.customer.id = ?1")
	public void deleteByCustomer(Integer customerId);
}
