package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

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
	public void AddType(String name) {
		ServiceTypeDto type = new ServiceTypeDto(name);
		types.save(type);
	}

	@Transactional
	public void SetType(Long id, String name) {
		ServiceTypeDto type = types.getById(id);
		if(name != null) type.SetName(name);
		types.save(type);
	}
	
	@Transactional
	public void DelType(String name) throws ParseException {
		ServiceTypeDto type = types.getOneByNameIgnoreCase(name);
		types.delete(type);
	}
	
	public List<ServiceTypeDto> GetTypes(){
		return types.findAll();
	}
	
	public ServiceTypeDto GetType(String name){
		return types.getOneByNameIgnoreCase(name);
	}
	
}
