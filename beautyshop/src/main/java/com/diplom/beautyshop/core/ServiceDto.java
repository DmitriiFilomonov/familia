package com.diplom.beautyshop.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Service")
public class ServiceDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkService;
	
	@Column
	public String name;
	
	@Column
	public Long length;
	
	@Column
	public Float price;
	
	@Column
	public Float discountPrice;
	
	@Column
	public Date discountDate;
	
	@Column
	public String sex;
	
	@ManyToOne
	@JoinColumn(name = "Service_Type")
	public ServiceTypeDto serviceType;
	
	@ManyToOne
	@JoinColumn(name = "Spec")
	public SpecDto spec;
	
	//Отзывы
	
	protected ServiceDto() {
		
	}
	
	public void SetSpec(SpecDto sp) {
		this.spec = sp;
	}
	
	public void SetName(String nam) {
		this.name = nam;
	}
	
	public void SetLength(Long len) {
		this.length = len;
	}
	
	public void SetPrice(Float price) {
		this.price = price;
	}
	
	public void SetDiscount(Float discount) {
		if(discount != null) this.discountPrice = discount / 100;
	}
	
	public void SetDiscountDate(Date dat) {
		this.discountDate = dat;
	}
	
	public void SetSex(String sex) {
		this.sex = sex;
	}
	
	public void SetType(ServiceTypeDto type) {
		this.serviceType = type;
	}
	
	public SpecDto GetSpec(){
		return spec;
	}
	
	public ServiceDto(String name, Long time, Float money, Float notMoney, Date dat, String sex, ServiceTypeDto type, SpecDto sp) {
		this.name = name;
		this.length = time;
		if(money != null) this.price = money;
		else this.price = (float) 0;
		if(notMoney != null) this.discountPrice = notMoney / 100;
		else this.discountPrice = (float) 0;
		this.discountDate = dat;
		this.sex = sex;
		this.serviceType = type;
		this.spec = sp;
	}
	
}

