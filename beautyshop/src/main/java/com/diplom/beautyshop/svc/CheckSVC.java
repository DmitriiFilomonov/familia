package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ProductInCheckDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.CheckRepo;
import com.diplom.beautyshop.repo.ClientRepo;
import com.diplom.beautyshop.repo.ProductInCheckRepo;
import com.diplom.beautyshop.repo.WorkerRepo;

import jakarta.transaction.Transactional;

@Service
public class CheckSVC {

	@Autowired
	private CheckRepo checks;
	
	@Autowired
	private ClientRepo clients;
	
	@Autowired
	private WorkerRepo workers;
	
	@Autowired
	private ClientSVC clientSVC;
	
	@Autowired
	private ProductInCheckRepo productsInCheck;
	
	@Transactional
	public void AddCheck(String date, Long state, Float dis, String client, String worker) throws ParseException {
		ClientDto c = clients.getOneByfioIgnoreCase(client);
		WorkerDto w = workers.getOneByfioIgnoreCase(worker);
		Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
		Long st = (long) 1;
		checks.save(new CheckDto(d, st, dis, c, w));
	}

	//Изментять колво товара
	@Transactional
	public void SetCheck(Long id, String date, Long state, Float dis, String client, String worker) throws ParseException {
		CheckDto ch = (CheckDto) Hibernate.unproxy(checks.getById(id));
		ClientDto c = null;
		if(client != null) c = clients.getOneByfioIgnoreCase(client);
		WorkerDto w = null;
		if(worker != null) w = workers.getOneByfioIgnoreCase(worker);
		Date d = null;
		if(date != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
		if(c != null) ch.client = c;
		if(d != null) ch.date = d;
		if(state != null) ch.state = state;
		if(dis != null) ch.discount = dis;
		if(w != null) ch.worker = w;
		checks.save(ch);
		clientSVC.UpdateType(ch.client.pkClient);
	}
	
	@Transactional
	public void DelCheck(Long id) {
		CheckDto ch = checks.getById(id);
		for(ProductInCheckDto prod : ch.prods) {
			productsInCheck.delete(prod);
		}
		checks.delete(ch);
	}
	
	public List<CheckDto> GetChecks(){
		return checks.findAll();
	}
	
	public List<CheckDto> GetChecksByWorker(String fio){
		WorkerDto w = workers.getOneByfioIgnoreCase(fio);
		List<CheckDto> r1 = checks.findAllByworker(w);
		return r1;
	}
	
	public List<CheckDto> GetChecksByClient(String name){
		ClientDto c = clients.getOneByfioIgnoreCase(name);
		List<CheckDto> r1 = checks.findAllByclient(c);
		return r1;
	}
	
	public CheckDto GetCheck(Long id){
		return checks.getById(id);
	}
	
}
