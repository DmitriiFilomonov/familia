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
@Table(name = "Client")
public class ClientDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkClient;
	
	@Column
	public String email;
	
	@Column
	public String number;
	
	@Column
	public String fio;
	
	@ManyToOne
	@JoinColumn(name = "ClientType")
	public ClientTypeDto clientType;
	
	public void SetFIO(String fio) {
		this.fio = fio;
	}
	
	protected ClientDto() {
		
	}
	
	public ClientDto(String email, String num, String name, ClientTypeDto type) {
		this.email = email;
		fio = name;
		this.number = num;
		this.clientType = type;
	}

}

