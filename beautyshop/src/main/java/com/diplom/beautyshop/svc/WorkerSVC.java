package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.RecordRepo;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.WorkerRepo;

import jakarta.transaction.Transactional;

@Service
public class WorkerSVC {

	@Autowired
	private WorkerRepo workers;
	
	@Autowired
	private ServiceRepo services;
	
	@Autowired
	private RecordRepo records;
	
	@Autowired
	private RecordSVC recordSVC;
	
	@Transactional
	public void AddWorker(String name, Long time, Float money, Float notMoney, String prof, String foto, List<String> servs) {
		WorkerDto worker = new WorkerDto(name, time, money, notMoney, prof, foto);
		workers.save(worker);
		if(servs != null) {
			ServiceDto service;
			List<WorkerDto> works;
			for(String x : servs){
				service = services.getOneByNameIgnoreCase(x);
				works = service.GetWorkers();
				works.add(worker);
				service.SetWorkers(works);
				services.save(service);
			}
		}
		workers.save(worker);
	}
	
	@Transactional
	public void AddService(String serv, String worker) throws ParseException {
		WorkerDto w = workers.getOneByfioIgnoreCase(worker);
		ServiceDto s = services.getOneByNameIgnoreCase(serv);
		s.workers.add(w);
		services.save(s);
	}
	
	@Transactional
	public void DelService(String serv, String worker) throws ParseException {
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
	public void DelWorker(Long id) {
		WorkerDto worker = (WorkerDto) Hibernate.unproxy(workers.getById(id));
		List<RecordDto> recs = records.findAllByworker(worker);
		for(RecordDto rec : recs) {
			recordSVC.DelRecord(rec.pkRecord);
		}
		List<ServiceDto> pastServs = worker.services;
		int i = 0;
		WorkerDto wor;
		for(ServiceDto s : pastServs) {
			i = 0;
			for(WorkerDto w : s.workers) {
				wor = (WorkerDto) Hibernate.unproxy(w);
				if(wor.pkWorker == id) { 
					s.workers.remove(i);
					break;
				}
				i++;
			}
			services.save(s);
		}
		workers.delete(worker);
	}

	@Transactional
	public void SetWorker(Long id, String name, Long time, Float money, Float notMoney, String prof, String foto, List<String> servs) {
		WorkerDto worker = (WorkerDto) Hibernate.unproxy(workers.getById(id));
		if(name != null) worker.SetFIO(name);
		if(foto != null) worker.SetFoto(foto);
		if(notMoney != null) worker.SetNotPaid(notMoney);
		if(prof != null)worker.SetProfile(prof);
		if(money != null)worker.SetSalary(money);
		if(time != null) worker.SetWorkTime(time);
		List<ServiceDto> pastServs = worker.services;
		int i = 0;
		WorkerDto wor;
		for(ServiceDto s : pastServs) {
			i = 0;
			for(WorkerDto w : s.workers) {
				wor = (WorkerDto) Hibernate.unproxy(w);
				if(wor.pkWorker == id) { 
					s.workers.remove(i);
					break;
				}
				i++;
			}
			services.save(s);
		}
		workers.save(worker);
		if(servs != null) {
			ServiceDto service;
			List<WorkerDto> works;
			for(String x : servs){
				service = services.getOneByNameIgnoreCase(x);
				works = service.GetWorkers();
				works.add(worker);
				service.SetWorkers(works);
				services.save(service);
			}
		}
	}
	
	public List<WorkerDto> GetWorkers(){
		return workers.findAll();
	}
	
	public List<WorkerDto> GetWorkersByService(String service){
		ServiceDto s = services.getOneByNameIgnoreCase(service);
		List<WorkerDto> w1 = workers.findAllByservices(s);
		List<WorkerDto> works = workers.findAll();
		ServiceDto serv = services.getOneByNameIgnoreCase(service);
		for(int i = 0; i < works.size(); i++) {
			if(!works.get(i).services.contains(serv)){
				works.remove(i);
				i--;
			}
		}
		return w1;
	}
	
	public WorkerDto GetWorker(String name){
		return workers.getOneByfioIgnoreCase(name);
	}
	
}
