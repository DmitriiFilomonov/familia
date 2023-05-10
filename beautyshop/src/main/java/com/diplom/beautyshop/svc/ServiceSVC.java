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
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.RecordRepo;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.ServiceTypeRepo;
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
	private WorkerRepo workers;
	
	@Autowired
	private RecordSVC recordSVC;
	
	//@Autowired
	//private ServiceTypeSVC typeSVC;
	
	@Transactional
	public void AddService(String name, Long time, Float price, Float discount, String dat, String sex, String type, List<String> workers) throws ParseException {
		ServiceTypeDto servType = null;
		if(type != null) servType = serviceTypes.getOneByNameIgnoreCase(type);
		Date d = null;
		if(dat != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
		List<WorkerDto> works = new ArrayList<WorkerDto>();
		WorkerDto w;
		if(workers != null) {
			for(String s : workers) {
				w = this.workers.getOneByfioIgnoreCase(s);
				works.add(w);
			}
		}
		services.save(new ServiceDto(name, time, price, discount, d, sex, servType, works));
	}
	
	//в AppSVC
	@Transactional
	public void AddServiceWithType(String name, Long time, Float price, Float discount, String dat, String sex, String type, List<String> workers) 
			throws ParseException {
		//typeSVC.AddType(name);
		ServiceTypeDto servType = new ServiceTypeDto();
		if(type != null) servType = serviceTypes.getOneByNameIgnoreCase(type);
		Date d = null;
		if(dat != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
		List<WorkerDto> works = new ArrayList<WorkerDto>();
		WorkerDto w;
		if(workers != null) {
			for(String s : workers) {
				w = this.workers.getOneByfioIgnoreCase(s);
				works.add(w);
			}
		}
		services.save(new ServiceDto(name, time, price, discount, d, sex, servType, works));
	}
	
	@Transactional
	public void AddWorker(String serv, String worker) throws ParseException {
		WorkerDto w = workers.getOneByfioIgnoreCase(worker);
		ServiceDto s = services.getOneByNameIgnoreCase(serv);
		s.workers.add(w);
		services.save(s);
	}
	
	@Transactional
	public void DelWorker(String serv, String worker) throws ParseException {
		WorkerDto wor;
		ServiceDto s = (ServiceDto) Hibernate.unproxy(services.getOneByNameIgnoreCase(serv));
		int i = 0;
		for(WorkerDto w : s.workers) {
			wor = (WorkerDto) Hibernate.unproxy(w);
			if(wor.fio == worker) { 
				s.workers.remove(i);
				break;
			}
			i++;
		}
		services.save(s);
	}
	
	@Transactional
	public void DelService(String name) throws ParseException {
		ServiceDto serv = services.getOneByNameIgnoreCase(name);
		List<RecordDto> recs = records.findAllByservice(serv);
		for(RecordDto rec : recs) {
			recordSVC.DelRecord(rec.pkRecord);
		}
		services.delete(serv);
	}
	
	@Transactional
	public void SetService(Long id, String name, Long time, Float price, Float discount, String dat, String sex, String type, List<String> workers)
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
		List<WorkerDto> works = new ArrayList<WorkerDto>();
		WorkerDto w;
		if(workers != null) {
			for(String s : workers) {
				w = this.workers.getOneByfioIgnoreCase(s);
				works.add(w);
			}
			serv.SetWorkers(works);
		}
		services.save(serv);
	}
	
	public List<ServiceDto> GetServices(){
		return services.findAll();
	}
	
	public ServiceDto GetService(String name){
		return services.getOneByNameIgnoreCase(name);
	}
	
	public List<ServiceDto> GetServicesByWorker(String fio){
		WorkerDto w = workers.getOneByfioIgnoreCase(fio);
		List<ServiceDto> s1 = services.findAllByworkers(w);
		List<ServiceDto> servs = services.findAll();
		WorkerDto worker = workers.getOneByfioIgnoreCase(fio);
		for(int i = 0; i < servs.size(); i++) {
			if(!servs.get(i).workers.contains(worker)){
				servs.remove(i);
				i--;
			}
		}
		return s1;
	}
	
	public List<ServiceDto> GetServicesByType(String type){
		ServiceTypeDto servType = serviceTypes.getOneByNameIgnoreCase(type);
		return services.findAllByserviceType(servType);
	}
	
}
