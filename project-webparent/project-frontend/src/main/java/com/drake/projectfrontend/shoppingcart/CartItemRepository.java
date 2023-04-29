package com.drake.projectfrontend.shoppingcart;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.drake.common.entity.CartItem;
import com.drake.common.entity.Customer;
import com.drake.common.entity.product.Product;


public interface CartItemRepository extends CrudRepository<CartItem, Integer>{

	public List<CartItem> findByCustomer (Customer customer);
	
	public CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1, c.size = ?4 WHERE c.customer.id= ?2 AND c.product.id = ?3")
	public void updateQuantity(Integer quantity, Integer customerId, Integer productId, Integer size);
	
	
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
	
	@Modifying
	@Query("DELETE CartItem c WHERE c.customer.id = ?1")
	public void deleteByCustomer(Integer customerId);
}
