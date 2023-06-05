package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.ServiceTypeRepo;

import jakarta.transaction.Transactional;

@Service
public class ServiceTypeSVC {

	@Autowired
	private ServiceTypeRepo types;
	
	@Autowired
	private ServiceRepo services;
	
	@Transactional
	public int AddType(String name) {
		ServiceTypeDto type = new ServiceTypeDto(name);
		if(types.getOneByNameIgnoreCase(name) == null) {
			types.save(type);
			return 1;
		}
		else return 2;
	}

	@Transactional
	public int SetType(Long id, String name) {
		try {
			ServiceTypeDto type = (ServiceTypeDto) Hibernate.unproxy(types.getById(id));
			if(name != null) {
				if(types.getOneByNameIgnoreCase(name) == null) {
					type.SetName(name);
				}
				else return 2;
			}
			types.save(type);
			return 1;
		}
		catch(Exception ex){
			return 2;
		}
	}
	
	@Transactional
	public int DelType(String name) throws ParseException {
		ServiceTypeDto type = types.getOneByNameIgnoreCase(name);
		if(type != null) {
			List<ServiceDto> servs = services.findAllByserviceType(type);
			for(ServiceDto serv : servs) {
				serv.serviceType = null;
			}
			types.delete(type);
			return 1;
		}
		else return 2;
	}
	
	public List<ServiceTypeDto> GetTypes(){
		return types.findAll();
	}
	
	public ServiceTypeDto GetType(String name){
		return types.getOneByNameIgnoreCase(name);
	}
	
}
