package com.drake.project.order;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.drake.common.entity.Country;
import com.drake.common.exception.OrderNotFoundException;
import com.drake.common.order.Order;
import com.drake.project.setting.country.CountryRepository;



@Service
public class OrderService {
public static final int ORDERS_PER_PAGE = 10;
	
	@Autowired private OrderRepository orderRepo;
	@Autowired private CountryRepository countryRepo;
	
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = null;
		if ("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));//sắp xếp các orders theo thứ tự country > state > city
		} else {
			sort = Sort.by(sortField);
		}
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

		if (keyword != null) {
			return orderRepo.findAll(keyword, pageable);
		}

		return orderRepo.findAll(pageable);
	}
	
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return orderRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id);
		}
	}
	
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = orderRepo.countById(id);
		if (count == null || count == 0) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id); 
		}
		
		orderRepo.deleteById(id);
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public void save(Order orderInForm) {
		Order orderInDB = orderRepo.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDB.getOrderTime());
		orderInForm.setCustomer(orderInDB.getCustomer());
		
		orderRepo.save(orderInForm);
	}	
}
