package com.diplom.beautyshop.core;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ClientType")
public class ClientTypeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkClientType;
	
	@Column
	public String name;
	
	@Column
	public Float discount;
	
	@Column
	public Long level;
	
	@Column
	public Float spend;
	
	@Column
	public Long went;
	
	@Column
	public Long time;
	
	protected ClientTypeDto() {
		
	}
	
	public ClientTypeDto(String name, Float dis, Long lev, Float spend, Long went, Long time) {
		this.name = name;
		if(discount != null) this.discount = dis / 100;
		else this.discount = (float) 0;
		this.level = lev;
		this.spend = spend;
		this.went = went;
		this.time = time;
	}
	
}
