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
	
	//@Autowired
	//private ServiceSVC serviceSVC;
	
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
	
	public List<ServiceTypeDto> GetTypes(){
		return types.findAll();
	}
	
	public ServiceTypeDto GetType(String name){
		return types.getOneByNameIgnoreCase(name);
	}
	
	@Transactional
	public void DelType(String name) throws ParseException {
		ServiceTypeDto type = types.getOneByNameIgnoreCase(name);
		types.delete(type);
	}
	
	//в AppSVC
	@Transactional
	public void DelTypeWithServices(String name) throws ParseException {
		ServiceTypeDto type = types.getOneByNameIgnoreCase(name);
		List<ServiceDto> servs = services.findAllByserviceType(type);
		//for(ServiceDto serv : servs) {
			//serviceSVC.DelService(serv.name);
		//}
		types.delete(type);
	}
	
}
