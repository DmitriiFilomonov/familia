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
@Table(name = "Spec")
public class SpecDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	public Long pkSpec;
	
	@Column
	public String name;
	
	@OneToMany(mappedBy="spec")
	public List<ServiceDto> servs;
	
	protected SpecDto() {
		
	}
	
	public SpecDto(String name) {
		this.name = name;
	}
	
	public void SetName(String nam) {
		name = nam;
	}
	
}
