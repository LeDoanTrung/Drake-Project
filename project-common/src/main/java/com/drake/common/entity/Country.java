package com.drake.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name = "countries")
public class Country extends IdBaseEntity{
	@Column(nullable = false, length = 45)
	private String name;
	
	@Column(nullable = false, length = 5)
	private String code;
	
	@OneToMany(mappedBy = "country")
	private Set<State> states;

	public Country(String name, String code, Set<State> states) {
		super();
		this.name = name;
		this.code = code;
		this.states = states;
	}

	public Country() {
		super();
	}
	
	public Country(Integer id) {
		this.id = id;
	}

	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}
	
	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public Country(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<State> getStates() {
		return states;
	}

	public void setStates(Set<State> states) {
		this.states = states;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}
}
