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
	public int AddCheck(String date, Long state, Float dis, String client, String worker) throws ParseException {
		ClientDto c = null;
		if(client != null) {
			c = clients.getOneByfioIgnoreCase(client);
			if(c == null) return 2;
		}
		WorkerDto w = null;
		if(worker != null) {
			w = workers.getOneByfioIgnoreCase(worker);
			if(w == null) return 2;
		}
		Date d = null;
		if(date != null) {
			try {
				d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
			}
			catch(Exception ex) {
				return 2;
			}
		}
		Long st = (long) 1;
		checks.save(new CheckDto(d, st, dis, c, w));
		return 1;
	}

	//Изментять колво товара
	@Transactional
	public int SetCheck(Long id, String date, Long state, Float dis, String client, String worker) throws ParseException {
		try {
			CheckDto ch = (CheckDto) Hibernate.unproxy(checks.getById(id));
			if(ch == null) return 2;
			ClientDto c = null;
			if(client != null) {
				c = clients.getOneByfioIgnoreCase(client);
				if(c == null) return 2;
			}
			WorkerDto w = null;
			if(worker != null) {
				w = workers.getOneByfioIgnoreCase(worker);
				if(w == null) return 2;
			}
			Date d = null;
			if(date != null) {
				try {
					d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
				}
				catch(Exception ex) {
					return 2;
				}
			}
			
			if(c != null) ch.client = c;
			if(d != null) ch.date = d;
			if(state != null) {
				List<ProductInCheckDto> prods = ch.prods;
				for(ProductInCheckDto prod : prods) {
					if(prod.lastAmount == null) {
						if(prod.amount > prod.prod.amount) return 2;
					}
					else {
						if((prod.prod.amount - (prod.amount - prod.lastAmount)) < 0) return 2; 
					}
				}
				for(ProductInCheckDto prod : prods) {
					prod.lastAmount = prod.amount;
					prod.prod.amount = prod.prod.amount - prod.amount;
				}
				ch.state = state;
			}
			if(dis != null) ch.discount = dis;
			if(w != null) ch.worker = w;
			checks.save(ch);
			clientSVC.UpdateType(ch.client.pkClient);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelCheck(Long id) {
		try {
			CheckDto ch = checks.getById(id);
			if(ch == null) return 2;
			for(ProductInCheckDto prod : ch.prods) {
				productsInCheck.delete(prod);
			}
			checks.delete(ch);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
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
