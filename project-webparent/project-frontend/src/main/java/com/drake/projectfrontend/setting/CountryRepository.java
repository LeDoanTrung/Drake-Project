package com.drake.projectfrontend.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.drake.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{
	@Query("SELECT c FROM Country c WHERE c.code = ?1")
	public Country findByCode(String code);
	
	public List<Country> findAllByOrderByNameAsc();
}
