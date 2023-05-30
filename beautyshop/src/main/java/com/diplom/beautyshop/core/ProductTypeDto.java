package com.diplom.beautyshop.core;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProductType")
public class ProductTypeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkProductType;
	
	@Column
	public String name;
	
	protected ProductTypeDto() {
		
	}
	
	public ProductTypeDto(String name) {
		this.name = name;
	}
}
