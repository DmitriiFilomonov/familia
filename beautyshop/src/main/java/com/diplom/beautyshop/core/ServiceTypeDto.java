package com.diplom.beautyshop.core;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Service_Type")
public class ServiceTypeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkServiceType;
	
	@Column
	public String name;
	
	public ServiceTypeDto() {
		
	}
	
	public ServiceTypeDto(String name) {
		this.name = name;
	}
	
	public void SetName(String nam) {
		name = nam;
	}
	
}
