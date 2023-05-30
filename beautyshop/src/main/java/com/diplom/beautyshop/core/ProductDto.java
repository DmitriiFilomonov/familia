package com.diplom.beautyshop.core;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Product")
public class ProductDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkProduct;
	
	@Column
	public String name;
	
	@Column
	public Float price;
	
	@Column
	public Float discountPrice;
	
	@Column
	public Date discountDate;
	
	@Column
	public Long amount;
	
	@ManyToOne
	@JoinColumn(name = "ProductType")
	public ProductTypeDto productType;
	
	protected ProductDto() {
		
	}
	
	public ProductDto(String name, Float price, Float discountPrice, Date discountDate, Long amount, ProductTypeDto type) {
		this.name = name;
		this.price = price;
		this.discountPrice = discountPrice;
		this.discountDate = discountDate;
		this.amount = amount;
		this.productType = type;
	}
}
