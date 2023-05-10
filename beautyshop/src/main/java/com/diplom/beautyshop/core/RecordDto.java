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
@Table(name = "Record")
public class RecordDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkRecord;
	
	@Column
	public Date date;
	
	@ManyToOne
	@JoinColumn(name = "Service")
	public ServiceDto service;
	
	@ManyToOne
	@JoinColumn(name = "Client")
	public ClientDto client;
	
	@ManyToOne
	@JoinColumn(name = "Worker")
	public WorkerDto worker;
	
	public void SetDate(Date dat) {
		this.date = dat;
	}
	
	public void SetService(ServiceDto serv) {
		this.service = serv;
	}
	
	public void SetClient(ClientDto client) {
		this.client = client;
	}
	
	public void SetWorker(WorkerDto worker) {
		this.worker = worker;
	}
	
	protected RecordDto() {
		
	}
	
	public RecordDto(Date dat, ServiceDto serv, ClientDto client, WorkerDto work) {
		this.date = dat;
		this.service = serv;
		this.client = client;
		this.worker = work;
	}
	
}
