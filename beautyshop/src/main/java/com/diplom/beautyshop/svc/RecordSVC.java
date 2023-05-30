package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.ClientRepo;
import com.diplom.beautyshop.repo.RecordRepo;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.WorkerRepo;

import jakarta.transaction.Transactional;

@Service
public class RecordSVC {

	@Autowired
	private ClientRepo clients;
	
	@Autowired
	private RecordRepo records;
	
	@Autowired
	private ClientSVC clientSVC;
	
	@Autowired
	private ServiceRepo services;
	
	@Autowired
	private WorkerRepo workers;
	
	@Transactional
	public boolean AddRecord(String dat, String serv, String client, String worker) throws ParseException {
		ClientDto c = clients.getOneByfioIgnoreCase(client);
		ServiceDto s = services.getOneByNameIgnoreCase(serv);
		WorkerDto w = workers.getOneByfioIgnoreCase(worker);
		Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
		if(CheckFreeTime(d, worker)) {
			Float pr = s.price * (s.discountPrice + c.clientType.discount);
			Long st = (long) 1;
			RecordDto r = new RecordDto(d, st, pr, s, c, w);
			records.save(r);
			clientSVC.UpdateType(c.pkClient);
			return true;
		}
		else return false;
	}

	@Transactional
	public void SetRecord(Long id, String dat, Long state, Float price, String serv, String client, String worker) throws ParseException {
		RecordDto rec = (RecordDto) Hibernate.unproxy(records.getById(id));
		ClientDto c = null;
		if(client != null) c = clients.getOneByfioIgnoreCase(client);
		ServiceDto s = null;
		if(serv != null) s = services.getOneByNameIgnoreCase(serv);
		WorkerDto w = null;
		if(worker != null) w = workers.getOneByfioIgnoreCase(worker);
		Date d = null;
		if(dat != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
		if(c != null) rec.SetClient(c);
		if(d != null) rec.SetDate(d);
		if(state != null) rec.state = state;
		if(price != null) rec.price = price;
		if(s != null) rec.SetService(s);
		if(w != null) rec.SetWorker(w);
		String work;
		if(w != null) work = worker;
		else work = rec.worker.fio;
		if(d != null) {
			if(CheckFreeTime(d, work)) {
				records.save(rec);
				clientSVC.UpdateType(rec.client.pkClient);
			}
		}
	}
	
	@Transactional
	public void DelRecord(Long id) {
		RecordDto rec = records.getById(id);
		records.delete(rec);
	}
	
	public List<RecordDto> GetRecords(){
		return records.findAll();
	}
	
	public List<RecordDto> GetRecordsByWorker(String fio){
		WorkerDto w = workers.getOneByfioIgnoreCase(fio);
		List<RecordDto> r1 = records.findAllByworker(w);
		List<RecordDto> recs = records.findAll();
		for(int i = 0; i < recs.size(); i++) {
			if(!(recs.get(i).worker.fio == fio)){
				recs.remove(i);
				i--;
			}
		}
		return r1;
	}
	
	public List<RecordDto> GetRecordsByService(String name){
		ServiceDto s = services.getOneByNameIgnoreCase(name);
		List<RecordDto> r1 = records.findAllByservice(s);
		List<RecordDto> recs = records.findAll();
		for(int i = 0; i < recs.size(); i++) {
			if(!(recs.get(i).service.name == name)){
				recs.remove(i);
				i--;
			}
		}
		return r1;
	}
	
	public List<RecordDto> GetRecordsByClient(String name){
		ClientDto c = clients.getOneByfioIgnoreCase(name);
		List<RecordDto> r1 = records.findAllByclient(c);
		List<RecordDto> recs = records.findAll();
		for(int i = 0; i < recs.size(); i++) {
			if(!(recs.get(i).client.fio == name)){
				recs.remove(i);
				i--;
			}
		}
		return r1;
	}
	
	public RecordDto GetRecord(Long id){
		return records.getById(id);
	}
	
	public List<RecordDto> GetRecordsByDate(String p1, String p2) throws ParseException{
		Date d1 = null, d2 = null;
		if(p1 != null && p2 != null) {
			d1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p1);
			d2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p2);
		}
		return records.findAllByDate(d1, d2);
	}
	
	public List<RecordDto> GetRecordsByDateAndWorker(String p1, String p2, String name) throws ParseException{
		Date d1 = null, d2 = null;
		if(p1 != null && p2 != null) {
			d1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p1);
			d2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p2);
		}
		return records.findAllByDateAndWorker(d1, d2, name);
	}
	
	public Boolean CheckFreeTime(Date p1, String name){
		List<RecordDto> r = records.checkFreeTime(p1, name);
		return r.size() == 0;
	}
}
