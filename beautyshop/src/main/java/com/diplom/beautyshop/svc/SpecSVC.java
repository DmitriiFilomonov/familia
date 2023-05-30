package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.repo.SpecRepo;

import jakarta.transaction.Transactional;

@Service
public class SpecSVC {

	@Autowired
	private SpecRepo specs;
	
	@Transactional
	public void AddSpec(String name) {
		SpecDto spec = new SpecDto(name);
		specs.save(spec);
	}

	@Transactional
	public void SetSpec(Long id, String name) {
		SpecDto spec = specs.getById(id);
		if(name != null) spec.SetName(name);
		specs.save(spec);
	}
	
	@Transactional
	public void DelSpec(String name) throws ParseException {
		SpecDto spec = specs.getOneByNameIgnoreCase(name);
		specs.delete(spec);
	}
	
	public List<SpecDto> GetSpecs(){
		return specs.findAll();
	}
	
	public SpecDto GetSpec(String name){
		return specs.getOneByNameIgnoreCase(name);
	}
	
}
