package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.SpecRepo;
import com.diplom.beautyshop.repo.WorkerRepo;

import jakarta.transaction.Transactional;

@Service
public class SpecSVC {

	@Autowired
	private SpecRepo specs;
	
	@Autowired
	private WorkerRepo workers;
	
	@Autowired
	private ServiceRepo services;
	
	@Transactional
	public int AddSpec(String name) {
		SpecDto spec = new SpecDto(name);
		if(specs.getOneByNameIgnoreCase(name) == null) {
			specs.save(spec);
			return 1;
		}
		else return 2;
	}

	@Transactional
	public int SetSpec(Long id, String name) {
		try {
			SpecDto spec = specs.getById(id);
			if(spec == null) return 2;
			if(name != null) {
				if(specs.getOneByNameIgnoreCase(name) != null) return 2;
				spec.SetName(name);
			}
			specs.save(spec);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelSpec(String name) throws ParseException {
		SpecDto spec = specs.getOneByNameIgnoreCase(name);
		if(spec != null) {
			List<WorkerDto> works = workers.findAllByspec(spec);
			for(WorkerDto work : works) {
				work.spec = null;
			}
			List<ServiceDto> servs = services.findAllByspec(spec);
			for(ServiceDto serv : servs) {
				serv.spec = null;
			}
			specs.delete(spec);
			return 1;
		}
		else return 2;
	}
	
	public List<SpecDto> GetSpecs(){
		return specs.findAll();
	}
	
	public SpecDto GetSpec(String name){
		return specs.getOneByNameIgnoreCase(name);
	}
	
}
