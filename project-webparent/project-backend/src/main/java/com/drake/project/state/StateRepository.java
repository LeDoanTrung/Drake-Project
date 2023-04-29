package com.drake.project.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.drake.common.entity.Country;
import com.drake.common.entity.State;


public interface StateRepository extends CrudRepository<State, Integer>{
	public List<State> findByCountryOrderByNameAsc(Country country);
}
