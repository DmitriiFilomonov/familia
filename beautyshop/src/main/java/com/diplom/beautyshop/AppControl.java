package com.diplom.beautyshop;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.svc.AppSVC;
import com.diplom.beautyshop.svc.ClientSVC;
import com.diplom.beautyshop.svc.RecordSVC;
import com.diplom.beautyshop.svc.ServiceSVC;
import com.diplom.beautyshop.svc.ServiceTypeSVC;
import com.diplom.beautyshop.svc.WorkerSVC;

@Controller
public class AppControl {

	@Autowired
	private ClientSVC clientSVC;
	
	@Autowired
	private RecordSVC recordSVC;
	
	@Autowired
	private ServiceSVC serviceSVC;
	
	@Autowired
	private ServiceTypeSVC serviceTypeSVC;
	
	@Autowired
	private WorkerSVC workerSVC;
	
	@Autowired
	private AppSVC appSVC;
	
	@RequestMapping(method=RequestMethod.GET, value = "/addServ")
	public void AddService(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="workers", required=false) List<String> workers) throws ParseException {
		serviceSVC.AddService(name, time, price, discount, dat, sex, type, workers);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setServ")
	public void SetService(@RequestParam(name="id") Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="time", required=false) Long time, @RequestParam(name="price", required=false) Float price, 
			@RequestParam(name="discount", required=false) Float discount, @RequestParam(name="date", required=false) String dat, 
			@RequestParam(name="sex", required=false) String sex, @RequestParam(name="type", required=false) String type,
			@RequestParam(name="workers", required=false) List<String> workers) throws ParseException {
		serviceSVC.SetService(id, name, time, price, discount, dat, sex, type, workers);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delServ")
	public void DelService(@RequestParam(name="name") String name) throws ParseException {
		serviceSVC.DelService(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servs")
	public List<ServiceDto> GetServices() {
		List<ServiceDto> aaa = serviceSVC.GetServices();
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/serv")
	public ServiceDto GetService(@RequestParam(name="name") String name) {
		ServiceDto aaa = serviceSVC.GetService(name);
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servicesByWorker")
	public List<ServiceDto> GetServicesByWorkers(@RequestParam(name="name") String name) {
		return serviceSVC.GetServicesByWorker(name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addServType")
	public void AddServiceType(@RequestParam(name="name") String name) throws ParseException {
		serviceTypeSVC.AddType(name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setServType")
	public void SetServiceType(@RequestParam(name="id") Long id, @RequestParam(name="name", required=false) String name) throws ParseException {
		serviceTypeSVC.SetType(id, name);
	}
	
	//Если нет сервисов
	@RequestMapping(method=RequestMethod.GET, value = "/delServType")
	public void DelServiceType(@RequestParam(name="name") String name) throws ParseException {
		serviceTypeSVC.DelType(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servTypes")
	public List<ServiceTypeDto> GetServiceTypes() {
		List<ServiceTypeDto> aaa = serviceTypeSVC.GetTypes();
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servType")
	public ServiceTypeDto GetServiceType(@RequestParam(name="name") String name) {
		ServiceTypeDto aaa = serviceTypeSVC.GetType(name);
		return aaa;
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addWorker")
	public void AddWorker(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="money", required=false) Float money, @RequestParam(name="notmoney", required=false) Float notMoney, 
			@RequestParam(name="prof", required=false) String prof, @RequestParam(name="foto", required=false) String foto, 
			@RequestParam(name="serv", required=false) List<String> serv) throws ParseException {
		workerSVC.AddWorker(name, time, money, notMoney, prof, foto, serv);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setWorker")
	public void SetWorker(@RequestParam(name="id") Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="time", required=false) Long time, @RequestParam(name="money", required=false) Float money, 
			@RequestParam(name="notmoney", required=false) Float notMoney, @RequestParam(name="prof", required=false) String prof, 
			@RequestParam(name="foto", required=false) String foto, @RequestParam(name="serv", required=false) List<String> serv) throws ParseException {
		workerSVC.SetWorker(id, name, time, money, notMoney, prof, foto, serv);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delWorker")
	public void DelWorker(@RequestParam(name="id") Long id) throws ParseException {
		workerSVC.DelWorker(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workers")
	public List<WorkerDto> GetWorker() {
		List<WorkerDto> aaa = workerSVC.GetWorkers();
		for(WorkerDto a : aaa) {
			a.services = null;
		}
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workersByService")
	public List<WorkerDto> GetWorkersByService(@RequestParam(name="name") String name) {
		return workerSVC.GetWorkersByService(name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addClient")
	public void AddClient(@RequestParam(name="name") String name, @RequestParam(name="info", required=false) String info) throws ParseException {
		clientSVC.AddClient(name, info);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setClient")
	public void SetClient(@RequestParam(name="id") Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="info", required=false) String info) throws ParseException {
		clientSVC.SetClient(id, name, info);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delClient")
	public void DelClient(@RequestParam(name="id") Long id) throws ParseException {
		clientSVC.DelClient(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clients")
	public List<ClientDto> GetClients() {
		List<ClientDto> aaa = clientSVC.GetClients();
		return aaa;
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addRecord")
	public void AddRecord(@RequestParam(name="date") String dat, @RequestParam(name="serv") String serv, @RequestParam(name="client") String cli, 
			@RequestParam(name="worker") String wor) throws ParseException {
		recordSVC.AddRecord(dat, serv, cli, wor);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setRecord")
	public void SetRecord(@RequestParam(name="id") Long id, @RequestParam(name="date", required=false) String dat, 
			@RequestParam(name="serv", required=false) String serv, @RequestParam(name="client", required=false) String cli, 
			@RequestParam(name="worker", required=false) String wor) throws ParseException {
		recordSVC.SetRecord(id, dat, serv, cli, wor);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delRecord")
	public void DelRecord(@RequestParam(name="id") Long id) throws ParseException {
		recordSVC.DelRecord(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/records")
	public List<RecordDto> GetRecords() {
		List<RecordDto> aaa = recordSVC.GetRecords();
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/record")
	public RecordDto GetRecord(@RequestParam(name="id") Long id) {
		RecordDto aaa = recordSVC.GetRecord(id);
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByWorker")
	public List<RecordDto> GetRecordsByWorker(@RequestParam(name="name") String name) {
		return recordSVC.GetRecordsByWorker(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByService")
	public List<RecordDto> GetRecordsByService(@RequestParam(name="name") String name) {
		return recordSVC.GetRecordsByService(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByClient")
	public List<RecordDto> GetRecordsByClient(@RequestParam(name="name") String name) {
		return recordSVC.GetRecordsByClient(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByDate")
	public List<RecordDto> GetRecordsByDate(@RequestParam(name="date1") String p1, @RequestParam(name="date2") String p2) throws ParseException {
		return recordSVC.GetRecordsByDate(p1, p2);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByDateAndWorker")
	public List<RecordDto> GetRecordsByDateAndWorker(@RequestParam(name="date1") String p1, @RequestParam(name="date2") String p2,
			@RequestParam(name="name") String name) throws ParseException {
		return recordSVC.GetRecordsByDateAndWorker(p1, p2, name);
	}
}
