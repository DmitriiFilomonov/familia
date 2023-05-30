package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ReviewDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.RecordRepo;
import com.diplom.beautyshop.repo.ReviewRepo;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.ServiceTypeRepo;
import com.diplom.beautyshop.repo.SpecRepo;
import com.diplom.beautyshop.repo.WorkerRepo;

import jakarta.transaction.Transactional;

@Service
public class ServiceSVC {

	@Autowired
	private ServiceTypeRepo serviceTypes;
	
	@Autowired
	private ServiceRepo services;
	
	@Autowired
	private RecordRepo records;
	
	@Autowired
	private ReviewRepo reviews;
	
	@Autowired
	private WorkerRepo workers;
	
	@Autowired
	private RecordSVC recordSVC;
	
	@Autowired
	private SpecRepo specs;
	
	@Autowired
	private ServiceTypeSVC typeSVC;
	
	@Autowired
	private SpecSVC specSVC;
	
	@Transactional
	public void AddService(String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec) throws ParseException {
		ServiceTypeDto servType = null;
		if(type != null) servType = serviceTypes.getOneByNameIgnoreCase(type);
		SpecDto sp = null;
		if(spec != null) sp = specs.getOneByNameIgnoreCase(spec);
		Date d = null;
		if(dat != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
		services.save(new ServiceDto(name, time, price, discount, d, sex, servType, sp));
	}
	
	@Transactional
	public void AddServiceWithType(String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec) 
			throws ParseException {
		typeSVC.AddType(type);
		AddService(name, time, price, discount, dat, sex, type, spec);
	}
	
	@Transactional
	public void AddServiceWithSpec(String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec) 
			throws ParseException {
		specSVC.AddSpec(spec);
		AddService(name, time, price, discount, dat, sex, type, spec);
	}
	
	@Transactional
	public void SetService(Long id, String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec)
			throws ParseException {
		ServiceDto serv = services.getById(id);
		if(name != null) serv.SetName(name);
		if(time != null) serv.SetLength(time);
		if(price != null) serv.SetPrice(price);
		if(discount != null) serv.SetDiscount(discount);
		if(dat != null) {
			Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
			serv.SetDiscountDate(d);
		}
		if(sex != null) serv.SetSex(sex);
		if(type != null) {
			ServiceTypeDto servType = serviceTypes.getOneByNameIgnoreCase(type);
			serv.SetType(servType);
		}
		if(spec != null) {
			SpecDto sp = specs.getOneByNameIgnoreCase(type);
			serv.SetSpec(sp);
		}
		services.save(serv);
	}

	@Transactional
	public void DelService(String name) throws ParseException {
		ServiceDto serv = services.getOneByNameIgnoreCase(name);
		List<RecordDto> recs = records.findAllByservice(serv);
		for(RecordDto rec : recs) {
			recordSVC.DelRecord(rec.pkRecord);
		}
		List<ReviewDto> revs = reviews.findAllByservice(serv);
		for(ReviewDto rev : revs) {
			reviews.delete(rev);
		}
		services.delete(serv);
	}
	
	public List<ServiceDto> GetServices(){
		return services.findAll();
	}
	
	public ServiceDto GetService(String name){
		return services.getOneByNameIgnoreCase(name);
	}
	
	public List<ServiceDto> GetServicesByType(String type){
		ServiceTypeDto servType = serviceTypes.getOneByNameIgnoreCase(type);
		return services.findAllByserviceType(servType);
	}
	
	public List<ServiceDto> GetServicesBySpec(String spec){
		SpecDto sp = specs.getOneByNameIgnoreCase(spec);
		return sp.servs;
	}
	
}
