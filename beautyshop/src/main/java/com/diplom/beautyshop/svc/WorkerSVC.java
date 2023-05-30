package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.CheckRepo;
import com.diplom.beautyshop.repo.RecordRepo;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.SpecRepo;
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
	
	@Autowired
	private SpecRepo specs;
	
	@Autowired
	private SpecSVC specSVC;
	
	@Autowired
	private CheckSVC checkSVC;
	
	@Autowired
	private CheckRepo checks;
	
	@Transactional
	public void AddWorker(String name, Long time, Float money, Float notMoney, String prof, String foto, String spec) {
		SpecDto sp = specs.getOneByNameIgnoreCase(spec);
		WorkerDto worker = new WorkerDto(name, time, money, notMoney, prof, foto, sp);
		workers.save(worker);
	}
	
	@Transactional
	public void AddWorkerWithSpec(String name, Long time, Float money, Float notMoney, String prof, String foto, String spec) {
		specSVC.AddSpec(spec);
		AddWorker(name, time, money, notMoney, prof, foto, spec);
	}

	@Transactional
	public void SetWorker(Long id, String name, Long time, Float money, Float notMoney, String prof, String foto, String spec) {
		WorkerDto worker = (WorkerDto) Hibernate.unproxy(workers.getById(id));
		if(name != null) worker.SetFIO(name);
		if(foto != null) worker.SetFoto(foto);
		if(notMoney != null) worker.SetNotPaid(notMoney);
		if(prof != null)worker.SetProfile(prof);
		if(money != null)worker.SetSalary(money);
		if(time != null) worker.SetWorkTime(time);
		if(spec != null) {
			SpecDto sp = specs.getOneByNameIgnoreCase(spec);
			worker.SetSpec(sp);
		}
		workers.save(worker);
	}
	
	@Transactional
	public void DelWorker(Long id) {
		WorkerDto worker = workers.getById(id);
		List<RecordDto> recs = records.findAllByworker(worker);
		for(RecordDto rec : recs) {
			recordSVC.DelRecord(rec.pkRecord);
		}
		List<CheckDto> chs = checks.findAllByworker(worker);
		for(CheckDto ch : chs) {
			checkSVC.DelCheck(ch.pkCheck);
		}
		workers.delete(worker);
	}
	
	public List<WorkerDto> GetWorkers(){
		return workers.findAll();
	}
	
	public WorkerDto GetWorker(String name){
		return workers.getOneByfioIgnoreCase(name);
	}
	
	public List<WorkerDto> GetWorkersBySpec(String spec){
		SpecDto sp = specs.getOneByNameIgnoreCase(spec);
		return workers.findAllByspec(sp);
	}
	
}
