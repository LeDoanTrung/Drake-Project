package com.drake.projectfrontend.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.drake.common.entity.CartItem;
import com.drake.common.entity.Customer;
import com.drake.common.entity.Rating;
import com.drake.common.entity.product.Product;
import com.drake.common.order.Order;
import com.drake.common.order.OrderDetail;
import com.drake.common.order.PaymentMethod;
import com.drake.projectfrontend.checkout.CheckoutInfo;


@Service
public class OrderService {

public static final int ORDERS_PER_PAGE = 5;
	
	@Autowired private OrderRepository repo;
	
	public Order createOrder(Customer customer, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		
		Order newOrder = new Order();
		
		newOrder.setOrderTime(new Date());
		newOrder.setCustomer(customer);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setSubtotal(checkoutInfo.getProductTotal());
		newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
		newOrder.setTax(0.0f);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setPaymentMethod(paymentMethod);
		
		newOrder.copyAddressFromCustomer();
		
		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			
			Rating rating = new Rating(product);
			product.setShopping(true);
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setSize(cartItem.getSize());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
			orderDetail.setSubtotal(cartItem.getSubtotal());
			orderDetail.setShippingCost(cartItem.getShippingCost());
			
			orderDetails.add(orderDetail);
		}
		
		return repo.save(newOrder);
	}
	
	public Page<Order> listForCustomerByPage(Customer customer, int pageNum,
			String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, ORDERS_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, customer.getId(), pageable);
		}
		
		return repo.findAll(customer.getId(), pageable);
	}
	
	
	public Order getOrder(Integer id, Customer customer) {
		return repo.findByIdAndCustomer(id, customer);
	}
}
