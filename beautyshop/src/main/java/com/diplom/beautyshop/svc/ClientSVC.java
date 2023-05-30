package com.diplom.beautyshop.svc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ClientTypeDto;
import com.diplom.beautyshop.core.ProductInCheckDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ReviewDto;
import com.diplom.beautyshop.repo.CheckRepo;
import com.diplom.beautyshop.repo.ClientRepo;
import com.diplom.beautyshop.repo.ClientTypeRepo;
import com.diplom.beautyshop.repo.ProductInCheckRepo;
import com.diplom.beautyshop.repo.RecordRepo;
import com.diplom.beautyshop.repo.ReviewRepo;

import jakarta.transaction.Transactional;

@Service
public class ClientSVC {

	@Autowired
	private ClientRepo clients;
	
	@Autowired
	private RecordRepo records;
	
	@Autowired
	private ReviewRepo reviews;
	
	@Autowired
	private ClientTypeRepo types;
	
	@Autowired
	private CheckRepo checks;
	
	@Autowired
	private ProductInCheckRepo productsInCheck;
	
	@Transactional
	public void AddClient(String email, String num, String name, String type) {
		ClientTypeDto clt = types.getOneByNameIgnoreCase(type);
		ClientDto client = new ClientDto(email, num, name, clt);
		clients.save(client);
	}
	
	@Transactional
	public void SetClient(Long id, String email, String num, String name, String type) {
		ClientDto c = clients.getById(id);
		if(email != null) c.email = email;
		if(num != null) c.number = num;
		if(name != null) c.fio = name;
		if(type != null) {
			ClientTypeDto tp = types.getOneByNameIgnoreCase(type);
			c.clientType = tp;
		}
		clients.save(c);
	}
	
	@Transactional
	public void UpdateType(Long id) {
		ClientDto c = clients.getById(id);
		List<RecordDto> recs = records.findAllByclient(c);
		int went;
		float spend;
		Date lastSpend = null;
		spend = went = 0;
		for(RecordDto rec : recs) {
			if(rec.state == 2) {
				if(lastSpend == null) {
					lastSpend = rec.date;
				}
				else if(rec.date.after(lastSpend)) {
					lastSpend = rec.date;
				}
				went++;
				spend += rec.price;
			}
		}
		List<CheckDto> chs = checks.findAllByclient(c);
		for(CheckDto ch : chs) {
			if(ch.state == 2) {
				if(lastSpend == null) {
					lastSpend = ch.date;
				}
				else if(ch.date.after(lastSpend)) {
					lastSpend = ch.date;
				}
				List<ProductInCheckDto> prods = productsInCheck.findAllBycheck(ch);
				float price = 0;
				for(ProductInCheckDto prod : prods) {
					price += prod.price * prod.amount;
				}
				price = price * ch.discount;
			}
		}
		Long maxLevel = (long) -1;
		ClientTypeDto newCl = null;
		Duration diff = Duration.between((Temporal) lastSpend, LocalDateTime.now());
		Long tim = diff.toDays();
		List<ClientTypeDto> tps = types.findAll();
		boolean f;
		for(ClientTypeDto ct : tps) {
			if(maxLevel < ct.level) {
				if((ct.spend <= spend || ct.went <= went) && ct.time >= tim) {
					newCl = ct;
					maxLevel = ct.level;
				}
			}
		}
		c.clientType = newCl;
		clients.save(c);
	}
	
	@Transactional
	public void DelClient(Long id) {
		ClientDto client = clients.getById(id);
		List<RecordDto> recs = records.findAllByclient(client);
		for(RecordDto rec : recs) {
			records.delete(rec);
		}
		List<ReviewDto> revs = reviews.findAllByclient(client);
		for(ReviewDto rev : revs) {
			reviews.delete(rev);
		}
		List<CheckDto> chs = checks.findAllByclient(client);
		for(CheckDto ch : chs) {
			List<ProductInCheckDto> prods = productsInCheck.findAllBycheck(ch);
			for(ProductInCheckDto prod : prods) {
				productsInCheck.delete(prod);
			}
			checks.delete(ch);
		}
		clients.delete(client);
	}
	
	public List<ClientDto> GetClients(){
		return clients.findAll();
	}
	
	public ClientDto GetClient(String fio){
		return clients.getOneByfioIgnoreCase(fio);
	}
	
	public List<ClientDto> GetClientsByType(String type){
		ClientTypeDto tp = types.getOneByNameIgnoreCase(type);
		return clients.findAllByclientType(tp);
	}
	
}
