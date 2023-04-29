package com.drake.common.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

//thuộc tính id dùng chung cho tất cả các entity --> tách ra thành 1 class riêng để các 
@MappedSuperclass
public class IdBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID tự động tăng
	
	protected Integer id;
	
	public Integer getId() {
		return id;
	}
	 
	public void setId(Integer id) {
		this.id = id;
	}
}
