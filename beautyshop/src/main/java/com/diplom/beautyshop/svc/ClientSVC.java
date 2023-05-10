package com.diplom.beautyshop.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.repo.ClientRepo;
import com.diplom.beautyshop.repo.RecordRepo;

import jakarta.transaction.Transactional;

@Service
public class ClientSVC {

	@Autowired
	private ClientRepo clients;
	
	@Autowired
	private RecordSVC recordSVC;
	
	@Autowired
	private RecordRepo records;
	
	@Transactional
	public void AddClient(String info, String fio) {
		ClientDto client = new ClientDto(info, fio);
		clients.save(client);
	}
	
	@Transactional
	public void DelClient(Long id) {
		ClientDto client = clients.getById(id);
		List<RecordDto> recs = records.findAllByclient(client);
		for(RecordDto rec : recs) {
			recordSVC.DelRecord(rec.pkRecord);
		}
		clients.delete(client);
	}

	@Transactional
	public void SetClient(Long id, String info, String fio) {
		ClientDto c = clients.getById(id);
		c.SetFIO(fio);
		c.SetInfo(info);
		clients.save(c);
	}
	
	public List<ClientDto> GetClients(){
		return clients.findAll();
	}
	
	public ClientDto GetClient(String fio){
		return clients.getOneByfioIgnoreCase(fio);
	}
	
}
