package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ProductDto;
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
	public int AddService(String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec) throws ParseException {
		ServiceTypeDto servType = null;
		if(type != null) {
			servType = serviceTypes.getOneByNameIgnoreCase(type);
			if(servType == null) return 2;
		}
		SpecDto sp = null;
		if(spec != null) {
			sp = specs.getOneByNameIgnoreCase(spec);
			if(sp == null) return 2;
		}
		Date d = null;
		try {
			if(dat != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
		}
		catch(Exception ex) {
			return 2;
		}
		if(services.getOneByNameIgnoreCase(name) != null) return 2;
		services.save(new ServiceDto(name, time, price, discount, d, sex, servType, sp));
		return 1;
	}
	
	@Transactional
	public int UpdateService() {
		List<ServiceDto> servs = services.findAll();
		Date dat = new Date();
		for(ServiceDto serv : servs) {
			if(dat.after(serv.discountDate)) {
				serv.discountPrice = null;
				serv.discountDate = null;
				services.save(serv);
			}
		}
		return 1;
	}
	
	@Transactional
	public int AddServiceWithType(String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec) 
			throws ParseException {
		int res = typeSVC.AddType(type);
		if(res == 1) {
			res = AddService(name, time, price, discount, dat, sex, type, spec);
		}
		return res;
	}
	
	@Transactional
	public int AddServiceWithSpec(String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec) 
			throws ParseException {
		int res = specSVC.AddSpec(spec);
		if(res == 1) {
			res = AddService(name, time, price, discount, dat, sex, type, spec);
		}
		return res;
	}
	
	@Transactional
	public int SetService(Long id, String name, Long time, Float price, Float discount, String dat, String sex, String type, String spec)
			throws ParseException {
		try {
			ServiceDto serv = (ServiceDto) Hibernate.unproxy(services.getById(id));
			if(serv == null) return 2;
			if(name != null) {
				if(services.getOneByNameIgnoreCase(name) != null) return 2;
			}
			ServiceTypeDto servType = null;
			if(type != null) {
				servType = serviceTypes.getOneByNameIgnoreCase(type);
				if(servType == null) return 2;
			}
			SpecDto sp = null;
			if(spec != null) {
				sp = specs.getOneByNameIgnoreCase(spec);
				if(sp == null) return 2;
			}
			if(dat != null) {
				Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
				serv.discountDate = d;
			}
			if(name != null) serv.name = name;
			if(type != null) serv.serviceType = servType;
			if(spec != null) serv.spec = sp;
			if(time != null) serv.length = time;
			if(price != null) serv.price = price;
			if(discount != null) serv.discountPrice = discount;
			if(sex != null) serv.sex = sex;
			services.save(serv);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}

	@Transactional
	public int DelService(String name) throws ParseException {
		ServiceDto serv = (ServiceDto) Hibernate.unproxy(services.getOneByNameIgnoreCase(name));
		if(serv == null) return 2;
		List<RecordDto> recs = records.findAllByservice(serv);
		for(RecordDto rec : recs) {
			recordSVC.DelRecord(rec.pkRecord);
		}
		List<ReviewDto> revs = reviews.findAllByservice(serv);
		for(ReviewDto rev : revs) {
			reviews.delete(rev);
		}
		services.delete(serv);
		return 1;
	}
	
	public List<ServiceDto> GetServices(Long f){
		List<ServiceDto> aaa = services.findAll();
		if(f == 1) Collections.sort(aaa, (o1, o2) -> o1.pkService.compareTo(o2.pkService));
		return aaa;
	}
	
	public ServiceDto GetService(String name){
		return services.getOneByNameIgnoreCase(name);
	}
	
	public ServiceDto GetService(Long id){
		return (ServiceDto) Hibernate.unproxy(services.getById(id));
	}
	
	public List<ServiceDto> GetServicesByType(String type, Long f){
		ServiceTypeDto servType = serviceTypes.getOneByNameIgnoreCase(type);
		List<ServiceDto> aaa = services.findAllByserviceType(servType);
		if(f == 1) Collections.sort(aaa, (o1, o2) -> o1.pkService.compareTo(o2.pkService));
		return aaa;
	}
	
	public List<ServiceDto> GetServicesBySpec(String spec, Long f){
		SpecDto sp = specs.getOneByNameIgnoreCase(spec);
		List<ServiceDto> aaa = sp.servs;
		if(f == 1) Collections.sort(aaa, (o1, o2) -> o1.pkService.compareTo(o2.pkService));
		return aaa;
	}
	
	public List<ServiceDto> GetServicesByType(Long id , Long f){
		ServiceTypeDto servType = (ServiceTypeDto) Hibernate.unproxy(serviceTypes.getById(id));
		List<ServiceDto> aaa = services.findAllByserviceType(servType);
		if(f == 1) Collections.sort(aaa, (o1, o2) -> o1.pkService.compareTo(o2.pkService));
		return aaa;
	}
	
	public List<ServiceDto> GetServicesBySpec(Long id, Long f){
		SpecDto sp = (SpecDto) Hibernate.unproxy(specs.getById(id));
		List<ServiceDto> aaa = sp.servs;
		if(f == 1) Collections.sort(aaa, (o1, o2) -> o1.pkService.compareTo(o2.pkService));
		return aaa;
	}
	
	public List<ServiceDto> GetServicesSortedById(){
		List<ServiceDto> aaa = services.findAll();
		Collections.sort(aaa, (o1, o2) -> o1.pkService.compareTo(o2.pkService));
		return aaa;
	}
	
}
