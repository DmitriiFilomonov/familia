package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ClientTypeDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.ClientRepo;
import com.diplom.beautyshop.repo.ClientTypeRepo;

import jakarta.transaction.Transactional;

@Service
public class ClientTypeSVC {

	@Autowired
	private ClientTypeRepo types;
	
	@Autowired
	private ClientRepo clients;
	
	@Transactional
	public int AddType(String name, Float dis, Long lev, Float spend, Long went, Long time) {
		ClientTypeDto ct = types.getOneBylevel(lev);
		if(ct == null) {
			types.save(new ClientTypeDto(name, dis, lev, spend, went, time));
			return 1;
		}
		else return 2;
	}
	
	@Transactional
	public int SetType(Long id, String name, Float dis, Long lev, Float spend, Long went, Long time) {
		ClientTypeDto ty = (ClientTypeDto) Hibernate.unproxy(types.getById(id));
		if(lev != null) {
			ClientTypeDto ct = types.getOneBylevel(lev);
			if(ct == null) ty.level = lev;
			else return 2;
		}
		if(name != null) {
			ClientTypeDto ct = types.getOneByNameIgnoreCase(name);
			if(ct == null) ty.name = name;
			else return 2;
		}
		if(dis != null) ty.discount = dis / 100;
		if(spend != null) ty.spend = spend;
		if(went != null) ty.went = went;
		if(time != null) ty.time = time;
		types.save(ty);
		return 1;
	}
	
	@Transactional
	public int DelType(Long id) {
		try {
			ClientTypeDto ty = (ClientTypeDto) Hibernate.unproxy(types.getById(id));
			if(ty != null) {
				List<ClientDto> cls = clients.findAllByclientType(ty);
				for(ClientDto cl : cls) {
					cl.clientType = null;
				}
				types.delete(ty);
				return 1;
			}
			else return 2;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	public List<ClientTypeDto> GetClientTypes(){
		return types.findAll();
	}
	
	public ClientTypeDto GetClientType(String name){
		return types.getOneByNameIgnoreCase(name);
	}
	
}
