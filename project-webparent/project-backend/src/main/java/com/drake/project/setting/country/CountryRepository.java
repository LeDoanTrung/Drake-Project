package com.drake.project.setting.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.drake.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{
	public List<Country> findAllByOrderByNameAsc();
}
