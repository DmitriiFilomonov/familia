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
	
	@ManyToMany
	@JoinTable(name = "Mid_Worker_Service", 
		joinColumns = { @JoinColumn(name = "Service") },
		inverseJoinColumns = { @JoinColumn(name = "Worker") })
	public List<WorkerDto> workers;
	
	protected ServiceDto() {
		
	}
	
	public void SetWorkers(List<WorkerDto> works) {
		this.workers = works;
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
		this.discountPrice = discount;
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
	
	public List<WorkerDto> GetWorkers(){
		return workers;
	}
	
	public ServiceDto(String name, Long time, Float money, Float notMoney, Date dat, String sex, ServiceTypeDto type) {
		this.name = name;
		this.length = time;
		this.price = money;
		this.discountPrice = notMoney;
		this.discountDate = dat;
		this.sex = sex;
		this.serviceType = type;
	}
	
	public ServiceDto(String name, Long time, Float money, Float notMoney, Date dat, String sex, ServiceTypeDto type, List<WorkerDto> ws) {
		this.name = name;
		this.length = time;
		this.price = money;
		this.discountPrice = notMoney;
		this.discountDate = dat;
		this.sex = sex;
		this.serviceType = type;
		this.workers = ws;
	}
}

