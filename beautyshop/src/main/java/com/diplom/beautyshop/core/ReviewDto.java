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
@Table(name = "Review")
public class ReviewDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkReview;
	
	@Column
	public String comment;
	
	@Column
	public Long score;
	
	@ManyToOne
	@JoinColumn(name = "Client")
	public ClientDto client;
	
	@ManyToOne
	@JoinColumn(name = "Service")
	public ServiceDto service;
	
	protected ReviewDto() {
		
	}
	
	public ReviewDto(String text, Long score, ClientDto client, ServiceDto service) {
		this.comment = text;
		this.score = score;
		this.client = client;
		this.service = service;
	}
	
}
