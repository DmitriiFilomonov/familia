package com.diplom.beautyshop.core;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Worker")
public class WorkerDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkWorker;
	
	@Column
	public String fio;
	
	@Column
	public Long workTime;
	
	@Column
	public Float salary;
	
	@Column
	public Float notPaid;
	
	@Column
	public String profile;
	
	@Column
	public String foto;
	
	@ManyToMany(mappedBy = "workers")
	public List<ServiceDto> services;
	
	public void SetWorkTime(Long time) {
		this.workTime = time;
	}
	
	public void SetFIO(String fio) {
		this.fio = fio;
	}
	
	public void SetSalary(Float sal) {
		this.salary = sal;
	}
	
	public void SetNotPaid(Float notPaid) {
		this.notPaid = notPaid;
	}
	
	public void SetProfile(String prof) {
		this.profile = prof;
	}
	
	public void SetFoto(String foto) {
		this.foto = foto;
	}
	
	protected WorkerDto() {
		
	}
	
	public WorkerDto(String name, Long time, Float money, Float notMoney, String prof, String fot) {
		this.fio = name;
		this.workTime = time;
		this.salary = money;
		this.notPaid = notMoney;
		this.profile = prof;
		this.foto = fot;
	}
}
