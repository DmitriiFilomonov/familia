package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.Collections;
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
	public int AddWorker(String name, Long time, Float money, Float notMoney, String prof, String foto, String login, String pass, String spec) {
		if(workers.getOneByfioIgnoreCase(name) != null) return 2;
		SpecDto sp = null;
		if(spec != null) {
			sp = specs.getOneByNameIgnoreCase(spec);
			if(sp == null) return 2;
		}
		WorkerDto worker = new WorkerDto(name, time, money, notMoney, prof, foto, login, pass, sp);
		workers.save(worker);
		return 1;
	}
	
	@Transactional
	public int AddWorkerWithSpec(String name, Long time, Float money, Float notMoney, String prof, String foto, String login, String pass, String spec) {
		int res = specSVC.AddSpec(spec);
		if(res == 1) {
			res = AddWorker(name, time, money, notMoney, prof, foto, login, pass, spec);
		}
		return res;
	}

	@Transactional
	public int SetWorker(Long id, String name, Long time, Float money, Float notMoney, String prof, String foto, String login, String pass, String spec) {
		try {
			WorkerDto worker = (WorkerDto) Hibernate.unproxy(workers.getById(id));
			if(worker == null) return 2;
			if(name != null) {
				if(workers.getOneByfioIgnoreCase(name) != null) return 2;
			}
			if(spec != null) {
				SpecDto sp = specs.getOneByNameIgnoreCase(spec);
				if(sp == null) return 2;
				worker.spec = sp;
			}
			if(name != null) worker.fio = name;
			if(foto != null) worker.foto = foto;
			if(notMoney != null) worker.notPaid = notMoney;
			if(prof != null)worker.profile = prof;
			if(money != null)worker.salary = money;
			if(time != null) worker.workTime = time;
			if(login != null) worker.login = login;
			if(pass != null) worker.pass = pass;
			workers.save(worker);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelWorker(Long id) {
		try {
			WorkerDto worker = (WorkerDto) Hibernate.unproxy(workers.getById(id));
			if(worker == null) return 2;
			List<RecordDto> recs = records.findAllByworker(worker);
			for(RecordDto rec : recs) {
				rec.worker = null;
			}
			List<CheckDto> chs = checks.findAllByworker(worker);
			for(CheckDto ch : chs) {
				ch.worker = null;
			}
			workers.delete(worker);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	public List<WorkerDto> GetWorkers(Long f){
		List<WorkerDto> aaa = workers.findAll();
		if(f == 1) {
			Collections.sort(aaa, (o1, o2) -> o1.pkWorker.compareTo(o2.pkWorker));
		}
		if(f == 2) {
			Collections.sort(aaa, (o1, o2) -> o1.fio.compareTo(o2.fio));
		}
		return aaa;
	}
	
	public WorkerDto GetWorker(String name){
		return workers.getOneByfioIgnoreCase(name);
	}
	
	public WorkerDto GetWorkerByLogin(String log){
		return workers.getOneByloginIgnoreCase(log);
	}
	
	public WorkerDto GetWorker(Long id){
		return (WorkerDto) Hibernate.unproxy(workers.getById(id));
	}
	
	public List<WorkerDto> GetWorkersBySpec(String spec, Long f){
		SpecDto sp = specs.getOneByNameIgnoreCase(spec);
		List<WorkerDto> aaa = workers.findAllByspec(sp);
		if(f == 1) {
			Collections.sort(aaa, (o1, o2) -> o1.pkWorker.compareTo(o2.pkWorker));
		}
		if(f == 2) {
			Collections.sort(aaa, (o1, o2) -> o1.fio.compareTo(o2.fio));
		}
		return aaa;
	}
	
	public List<WorkerDto> GetWorkersBySpec(Long spec, Long f){
		SpecDto sp = specs.getById(spec);
		List<WorkerDto> aaa = workers.findAllByspec(sp);
		if(f == 1) {
			Collections.sort(aaa, (o1, o2) -> o1.pkWorker.compareTo(o2.pkWorker));
		}
		if(f == 2) {
			Collections.sort(aaa, (o1, o2) -> o1.fio.compareTo(o2.fio));
		}
		return aaa;
	}
	
	public List<WorkerDto> GetWorkersSortedById(){
		List<WorkerDto> aaa = workers.findAll();
		Collections.sort(aaa, (o1, o2) -> o1.pkWorker.compareTo(o2.pkWorker));
		return aaa;
	}
	
	public List<WorkerDto> GetWorkersSortedByFIO(){
		List<WorkerDto> aaa = workers.findAll();
		Collections.sort(aaa, (o1, o2) -> o1.fio.compareTo(o2.fio));
		return aaa;
	}
	
}
