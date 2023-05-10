package com.diplom.beautyshop.core;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Client")
public class ClientDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkClient;
	
	@Column
	public String contactInfo;
	
	@Column
	public String fio;
	
	public void SetInfo(String info) {
		this.contactInfo = info;
	}
	
	public void SetFIO(String fio) {
		this.fio = fio;
	}
	
	protected ClientDto() {
		
	}
	
	public ClientDto(String info) {
		this.contactInfo = info;
		fio = "";
	}
	
	public ClientDto(String info, String name) {
		this.contactInfo = info;
		fio = name;
	}
}

