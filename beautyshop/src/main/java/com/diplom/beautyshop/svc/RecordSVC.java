package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
	public int AddRecord(String dat, String serv, String client, String worker) throws ParseException {
		ClientDto c = clients.getOneByfioIgnoreCase(client);
		ServiceDto s = services.getOneByNameIgnoreCase(serv);
		WorkerDto w = workers.getOneByfioIgnoreCase(worker);
		if(c == null || s == null || w == null) return 2;
		try {
			Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
			if(CheckFreeTime(d, worker)) {
				Float pr;
				if(c.clientType != null) pr = s.price * (1 - s.discountPrice - c.clientType.discount);
				else pr = s.price * (1 - s.discountPrice);
				Long st = (long) 1;
				RecordDto r = new RecordDto(d, st, pr, s, c, w);
				records.save(r);
				clientSVC.UpdateType(c.pkClient);
				return 1;
			}
			else return 3;
		}
		catch(Exception ex) {
			return 4;
		}
	}
	
	@Transactional
	public int UpdatePrice(Long id) {
		try {
			RecordDto rec = (RecordDto) Hibernate.unproxy(records.getById(id));
			if(rec == null) return 2;
			if(rec.state != 1) return 2;
			Float pr;
			if(rec.client.clientType != null) pr = rec.service.price * (1 - rec.service.discountPrice - rec.client.clientType.discount);
			else pr = rec.service.price * (1 - rec.service.discountPrice);
			rec.price = pr;
			records.save(rec);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}

	@Transactional
	public int SetRecord(Long id, String dat, Long state, Float price, String serv, String client, String worker) throws ParseException {
		try {
			RecordDto rec = (RecordDto) Hibernate.unproxy(records.getById(id));
			ClientDto c = null;
			if(client != null) {
				c = clients.getOneByfioIgnoreCase(client);
				if(c == null) return 2;
			}
			ServiceDto s = null;
			if(serv != null) {
				s = services.getOneByNameIgnoreCase(serv);
				if(s == null) return 2;
			}
			WorkerDto w = null;
			if(worker != null) {
				w = workers.getOneByfioIgnoreCase(worker);
				if(w == null) return 2;
			}
			Date d = null;
			if(dat != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dat);
			String work;
			if(w != null) work = worker;
			else work = rec.worker.fio;
			if(d != null) {
				if(!CheckFreeTime(d, work)) return 2;
			}
			if(rec.state == 1) {
				if(c != null) rec.SetClient(c);
				if(d != null) rec.SetDate(d);
				if(price != null) rec.price = price;
				if(s != null) rec.SetService(s);
				if(w != null) rec.SetWorker(w);
				if(price == null && (s != null || c != null)) UpdatePrice(rec.pkRecord);
			}
			if(state != null) rec.state = state;
			clientSVC.UpdateType(rec.client.pkClient);
			records.save(rec);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelRecord(Long id) {
		try {
			RecordDto rec = (RecordDto) Hibernate.unproxy(records.getById(id));
			if(rec != null) {
				records.delete(rec);
				return 1;
			}
			else return 2;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	public List<RecordDto> GetRecords(){
		return records.findAll();
	}
	
	public List<RecordDto> GetRecordsByWorker(String fio){
		WorkerDto w = workers.getOneByfioIgnoreCase(fio);
		if(w != null) {
			List<RecordDto> r1 = records.findAllByworker(w);
			return r1;
		}
		else {
			return new ArrayList<RecordDto>();
		}
	}
	
	public List<RecordDto> GetRecordsByService(String name){
		ServiceDto s = services.getOneByNameIgnoreCase(name);
		if(s != null) {
			List<RecordDto> r1 = records.findAllByservice(s);
			return r1;
		}
		else {
			return new ArrayList<RecordDto>();
		}
	}
	
	public List<RecordDto> GetRecordsByClient(String name){
		ClientDto c = clients.getOneByfioIgnoreCase(name);
		if(c != null) {
			List<RecordDto> r1 = records.findAllByclient(c);
			return r1;
		}
		else {
			return new ArrayList<RecordDto>();
		}
	}
	
	public RecordDto GetRecord(Long id){
		try {
			return records.getById(id);
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	public List<RecordDto> GetRecordsByDate(String p1, String p2) throws ParseException{
		try {
			Date d1 = null, d2 = null;
			if(p1 != null && p2 != null) {
				d1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p1);
				d2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p2);
			}
			return records.findAllByDate(d1, d2);
		}
		catch(Exception ex) {
			return new ArrayList<RecordDto>();
		}
	}
	
	public List<RecordDto> GetRecordsByDateAndWorker(String p1, String p2, String name) throws ParseException{
		try {
			Date d1 = null, d2 = null;
			if(p1 != null && p2 != null) {
				d1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p1);
				d2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p2);
			}
			WorkerDto w = workers.getOneByfioIgnoreCase(name);
			if(w == null) return new ArrayList<RecordDto>();
			return records.findAllByDateAndWorker(d1, d2, name);
		}
		catch(Exception ex) {
			return new ArrayList<RecordDto>();
		}
	}
	
	public Boolean CheckFreeTime(Date p1, String name){
		WorkerDto w = workers.getOneByfioIgnoreCase(name);
		if(w == null) return false;
		List<RecordDto> r = records.checkFreeTime(p1, name);
		return r.size() == 0;
	}
	
	public List<Date> GetFreeTime(String p, String name) throws ParseException{
		Date d1 = new SimpleDateFormat("dd-MM-yyyy").parse(p);
		String p1 = p + " 10:00";
		String p2 = p + " 17:00";
		List<RecordDto> recs = GetRecordsByDateAndWorker(p1, p2, name);
		Collections.sort(recs, (o1, o2) -> o1.date.compareTo(o2.date));
		List<Date> res = new ArrayList<Date>();
		Date a1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p1);
		Date a2 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(p2);
		RecordDto rec;
		while(a1.before(a2)) {
			if(recs.size() == 0) {
				res.add((Date) a1.clone());
				if(a1.getMinutes() < 30) {
					a1.setMinutes(30);
				}
				else {
					a1.setHours(a1.getHours() + 1);
					a1.setMinutes(0);
				}
			}
			else {
				if(a1.before(recs.get(0).date)) {
					res.add((Date) a1.clone());
					if(a1.getMinutes() < 30) {
						a1.setMinutes(30);
					}
					else {
						a1.setHours(a1.getHours() + 1);
						a1.setMinutes(0);
					}
				}
				else {
					rec = recs.get(0);
					a1 = rec.date;
					a1.setHours(a1.getHours() + rec.service.length.intValue() / 60);
					a1.setMinutes(a1.getMinutes() + rec.service.length.intValue() % 60);
					recs.remove(0);
					if(a1.getMinutes() < 30) {
						a1.setMinutes(30);
					}
					else {
						a1.setHours(a1.getHours() + 1);
						a1.setMinutes(0);
					}
				}
			}
			
		}
		return res;
	}
}
