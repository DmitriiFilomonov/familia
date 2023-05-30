package com.diplom.beautyshop.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Checkk")
public class CheckDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	public Long pkCheck;
	
	@Column
	public Date date;
	
    @Column
    public Long state;
    
    @Column
    public Float discount;

    @OneToMany(mappedBy="check")
	public List<ProductInCheckDto> prods;
    
    @ManyToOne
	@JoinColumn(name = "Client")
	public ClientDto client;
    
    @ManyToOne
	@JoinColumn(name = "Worker")
	public WorkerDto worker;
    
    protected CheckDto() {
    	
    }
    
    public CheckDto(Date date, Long state, Float dis, ClientDto client, WorkerDto worker) {
    	this.date = date;
    	this.state = state;
    	this.discount = dis;
    	this.client = client;
    	this.worker = worker;
    }
}
