package com.diplom.beautyshop.core;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProductInCheck")
public class ProductInCheckDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkProductInCheck;
	
	@ManyToOne
	@JoinColumn(name = "Product")
	public ProductDto prod;
	
	@Column
	public Long amount;
	
	@Column
	public Long lastAmount;
	
	@Column
	public Float price;
	
	@ManyToOne
	@JoinColumn(name = "Checkk")
	public CheckDto check;

	protected ProductInCheckDto() {
		
	}
	
	public ProductInCheckDto(ProductDto prod, Long amount, Float price, CheckDto check) {
		this.prod = prod;
		this.amount = amount;
		this.price = price;
		this.check = check;
	}
}
