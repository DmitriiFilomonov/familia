package com.diplom.beautyshop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.nio.file.Files;
import java.nio.file.spi.FileTypeDetector;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Hibernate;
import org.json.*;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ClientTypeDto;
import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductInCheckDto;
import com.diplom.beautyshop.core.ProductTypeDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ReviewDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.svc.AppSVC;
import com.diplom.beautyshop.svc.CheckSVC;
import com.diplom.beautyshop.svc.ClientSVC;
import com.diplom.beautyshop.svc.ClientTypeSVC;
import com.diplom.beautyshop.svc.ProductInCheckSVC;
import com.diplom.beautyshop.svc.ProductSVC;
import com.diplom.beautyshop.svc.ProductTypeSVC;
import com.diplom.beautyshop.svc.RecordSVC;
import com.diplom.beautyshop.svc.ReviewSVC;
import com.diplom.beautyshop.svc.ServiceSVC;
import com.diplom.beautyshop.svc.ServiceTypeSVC;
import com.diplom.beautyshop.svc.SpecSVC;
import com.diplom.beautyshop.svc.WorkerSVC;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServlet;

@Controller
public class AppControl {
	
	@Autowired
	private HttpServlet request;

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
	
	@Autowired
	private CheckSVC checkSVC;
	
	@Autowired
	private ClientTypeSVC clientTypeSVC;
	
	@Autowired
	private ProductInCheckSVC productInCheckSVC;
	
	@Autowired
	private ProductSVC productSVC;
	
	@Autowired
	private ReviewSVC reviewSVC;
	
	@Autowired
	private SpecSVC specSVC;
	
	@Autowired
	private ProductTypeSVC productTypeSVC;
	
	@RequestMapping(method=RequestMethod.GET, value = "/hhru")
	public String Index() {
		return "index.html";
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/adminPidor")
	public String Admin() {
		return "adminRecord.html";
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/im", produces=MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] GetImg() throws IOException {
		InputStream in = new FileInputStream("/s1.png");
		return in.readAllBytes();
	}
	
	//На всякий
	@RequestMapping(method=RequestMethod.GET, value = "/imm")
	public ResponseEntity<byte[]> GetIm() throws IOException {
		File in = new File("D:\\s1.png");
		return ResponseEntity.ok().contentType(MediaType.valueOf(in.toURL().openConnection().getContentType())).body(Files.readAllBytes(in.toPath()));
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addCheck")
	public void AddCheck(@RequestParam(name="date") String date, @RequestParam(name="state") Long state, @RequestParam(name="discount") Float dis, 
			@RequestParam(name="client") String client, @RequestParam(name="worker") String worker) throws ParseException {
		checkSVC.AddCheck(date, state, dis, client, worker);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setCheck")
	public void SetCheck(@RequestParam(name="id") Long id, @RequestParam(name="date") String date, @RequestParam(name="state") Long state, 
			@RequestParam(name="discount") Float dis, @RequestParam(name="client") String client, 
			@RequestParam(name="worker") String worker) throws ParseException {
		checkSVC.SetCheck(id, date, state, dis, client, worker);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delCheck")
	public void DelCheck(@RequestParam(name="id") Long id) throws ParseException {
		checkSVC.DelCheck(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/checks")
	public List<CheckDto> GetChecks() {
		return checkSVC.GetChecks();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/checksByClient")
	public List<CheckDto> GetChecksByClient(@RequestParam(name="name") String name) {
		return checkSVC.GetChecksByClient(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/checksByWorker")
	public List<CheckDto> GetChecksByWorker(@RequestParam(name="name") String name) {
		return checkSVC.GetChecksByWorker(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/check")
	public CheckDto GetCheck(@RequestParam(name="id") Long id) {
		return checkSVC.GetCheck(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addServ")
	public void AddService(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		serviceSVC.AddService(name, time, price, discount, dat, sex, type, spec);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addServWithType")
	public void AddServiceWithType(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		serviceSVC.AddServiceWithType(name, time, price, discount, dat, sex, type, spec);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addServWithSpec")
	public void AddServiceWithSpec(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		serviceSVC.AddServiceWithSpec(name, time, price, discount, dat, sex, type, spec);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setServ")
	public void SetService(@RequestParam(name="id") Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="time", required=false) Long time, @RequestParam(name="price", required=false) Float price, 
			@RequestParam(name="discount", required=false) Float discount, @RequestParam(name="date", required=false) String dat, 
			@RequestParam(name="sex", required=false) String sex, @RequestParam(name="type", required=false) String type,
			@RequestParam(name="spec", required=false) String spec) throws ParseException {
		serviceSVC.SetService(id, name, time, price, discount, dat, sex, type, spec);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delServ")
	public void DelService(@RequestParam(name="name") String name) throws ParseException {
		serviceSVC.DelService(name);
	}
	
	public JSONObject CreateCheckJSON(CheckDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkCheck", obj.pkCheck);
		js.put("date", obj.date);
		js.put("state", obj.state);
		js.put("discount", obj.discount);
		JSONArray jsArr = new JSONArray();
		JSONObject j = new JSONObject();
		for(ProductInCheckDto pro : obj.prods) {
			j.clear();
			j.put("pkProductInCheck", pro.pkProductInCheck);
			j.put("name", pro.prod.name);
			j.put("amount", pro.amount);
			j.put("price", pro.price);
			jsArr.put(j);
		}
		js.put("prods", jsArr);
		j.clear();
		j.put("pkClient", obj.client.pkClient);
		j.put("fio", obj.client.fio);
		js.put("client", j);
		j.clear();
		j.put("pkWorker", obj.worker.pkWorker);
		j.put("fio", obj.worker.fio);
		js.put("worker", j);
		return js;
	}
	
	public JSONObject CreateClientJSON(ClientDto cli) {
		JSONObject js = new JSONObject();
		js.put("pkClient", cli.pkClient);
		js.put("fio", cli.fio);
		js.put("email", cli.email);
		js.put("number", cli.number);
		JSONObject j = new JSONObject();
		j.put("pkClientType", cli.clientType.pkClientType);
		j.put("name", cli.clientType.name);
		js.put("type", j);
		return js;
	}
	
	public JSONObject CreateClientTypeJSON(ClientTypeDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkClientType", obj.pkClientType);
		js.put("name", obj.name);
		js.put("discount", obj.discount);
		js.put("level", obj.level);
		js.put("spend", obj.spend);
		js.put("went", obj.went);
		js.put("time", obj.time);
		return js;
	}
	
	public JSONObject CreateProductJSON(ProductDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkProduct", obj.pkProduct);
		js.put("name", obj.name);
		js.put("price", obj.price);
		js.put("discountPrice", obj.discountPrice);
		js.put("discountDate", obj.discountDate);
		js.put("amount", obj.amount);
		JSONObject j = new JSONObject();
		if(obj.productType != null) {
			j.put("pkProductType", obj.productType.pkProductType);
			j.put("name", obj.productType.name);
			js.put("type", j);
		}
		else js.put("type", "null");
		return js;
	}
	
	public JSONObject CreateProductInCheckJSON(ProductInCheckDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkProductInCheck", obj.pkProductInCheck);
		JSONObject j = new JSONObject();
		j.put("pkProduct", obj.prod.pkProduct);
		j.put("name", obj.prod.name);
		js.put("product", j);
		js.put("amount", obj.amount);
		js.put("price", obj.price);
		js.put("check", obj.check.pkCheck);
		return js;
	}
	
	public JSONObject CreateProductTypeJSON(ProductTypeDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkProductType", obj.pkProductType);
		js.put("name", obj.name);
		return js;
	}
	
	public JSONObject CreateRecordJSON(RecordDto rec) {
		JSONObject js = new JSONObject();
		js.put("pkRecord", rec.pkRecord);
		js.put("date", rec.date);
		js.put("state", rec.state);
		js.put("price", rec.price);
		JSONObject j = new JSONObject();
		j.put("pkService", rec.service.pkService);
		j.put("name", rec.service.name);
		js.put("service", j);
		j.clear();
		j.put("pkWorker", rec.worker.pkWorker);
		j.put("fio", rec.worker.fio);
		js.put("worker", j);
		j.clear();
		j.put("pkClient", rec.client.pkClient);
		j.put("fio", rec.client.fio);
		js.put("client", j);
		return js;
	}
	
	public JSONObject CreateReviewJSON(ReviewDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkReview", obj.pkReview);
		js.put("comment", obj.comment);
		js.put("score", obj.score);
		JSONObject j = new JSONObject();
		j.put("pkService", obj.service.pkService);
		j.put("name", obj.service.name);
		js.put("service", j);
		j.clear();
		j.put("pkClient", obj.client.pkClient);
		j.put("fio", obj.client.fio);
		js.put("client", j);
		return js;
	}
	
	public JSONObject CreateServiceJSON(ServiceDto serv) {
		JSONObject js = new JSONObject();
		JSONObject j = new JSONObject();
		js.put("pkService", serv.pkService);
		js.put("name", serv.name);
		js.put("length", serv.length);
		js.put("price", serv.price);
		js.put("discountPrice", serv.discountPrice);
		js.put("discountDate", serv.discountDate);
		js.put("sex", serv.sex);
		if(serv.serviceType != null) {
			j.put("pkServiceType", serv.serviceType.pkServiceType);
			j.put("name", serv.serviceType.name);
			js.put("serviceType", j);
		}
		else js.put("serviceType", "null");
		j.clear();
		if(serv.spec != null) {
			j.put("pkSpec", serv.spec.pkSpec);
			j.put("name", serv.spec.name);
			js.put("spec", j);
		}
		else js.put("spec", "null");
		return js;
	}
	
	public JSONObject CreateServiceTypeJSON(ServiceTypeDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkServiceType", obj.pkServiceType);
		js.put("name", obj.name);
		return js;
	}
	
	public JSONObject CreateSpecJSON(SpecDto obj) {
		JSONObject js = new JSONObject();
		js.put("pkSpec", obj.pkSpec);
		js.put("name", obj.name);
		JSONArray jsArr = new JSONArray();
		JSONObject j = new JSONObject();
		for(ServiceDto serv : obj.servs) {
			j.clear();
			j.put("pkService", serv.pkService);
			j.put("name", serv.name);
			jsArr.put(j);
		}
		js.put("services", jsArr);
		return js;
	}
	
	public JSONObject CreateWorkerJSON(WorkerDto work) {
		JSONObject js = new JSONObject();
		js.put("pkWorker", work.pkWorker);
		js.put("fio", work.fio);
		js.put("foto", work.foto);
		js.put("workTime", work.workTime);
		js.put("salary", work.salary);
		js.put("notPaid", work.notPaid);
		js.put("profile", work.profile);
		JSONObject j = new JSONObject();
		if(work.spec != null) {
			j.put("pkSpec", work.spec.pkSpec);
			j.put("name", work.spec.name);
			js.put("spec", j);
		}
		else js.put("spec", "null");
		return js;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servs")
	public List<ServiceDto> GetServices() {
		return serviceSVC.GetServices();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsBySpec")
	public List<ServiceDto> GetServicesBySpec(@RequestParam(name="spec") String spec) {
		return serviceSVC.GetServicesBySpec(spec);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsByType")
	public List<ServiceDto> GetServicesByType(@RequestParam(name="type") String type) {
		return serviceSVC.GetServicesByType(type);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/serv")
	public ServiceDto GetService(@RequestParam(name="name") String name) {
		return serviceSVC.GetService(name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addSpec")
	public void AddSpec(@RequestParam(name="name") String name) throws ParseException {
		specSVC.AddSpec(name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setSpec")
	public void SetSpec(@RequestParam(name="id") Long id, @RequestParam(name="name") String name) throws ParseException {
		specSVC.SetSpec(id, name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delSpec")
	public void DelSpec(@RequestParam(name="name") String name) throws ParseException {
		specSVC.DelSpec(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/specs")
	public List<SpecDto> GetSpecs() {
		List<SpecDto> aaa = specSVC.GetSpecs();
		return aaa;
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/spec")
	public SpecDto GetSpec(@RequestParam(name="name") String name) {
		return specSVC.GetSpec(name);
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
	
	@RequestMapping(method=RequestMethod.POST, value = "/addWorker")
	public void AddWorker(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="money", required=false) Float money, @RequestParam(name="notmoney", required=false) Float notMoney, 
			@RequestParam(name="prof", required=false) String prof, @RequestParam(name="foto", required=false) MultipartFile foto, 
			@RequestParam(name="spec", required=false) String spec) throws ParseException, IllegalStateException, IOException {
	    String aaa = foto.getOriginalFilename();
	    foto.transferTo(new File("/img/" + aaa));
		workerSVC.AddWorker(name, time, money, notMoney, prof, aaa, spec);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/addWorkerWithSpec")
	public void AddWorkerWithSpec(@RequestParam(name="name") String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="money", required=false) Float money, @RequestParam(name="notmoney", required=false) Float notMoney, 
			@RequestParam(name="prof", required=false) String prof, @RequestParam(name="foto", required=false) MultipartFile foto, 
			@RequestParam(name="spec", required=false) String spec) throws ParseException, IllegalStateException, IOException {
	    String aaa = foto.getOriginalFilename();
	    foto.transferTo(new File("/img/" + aaa));
		workerSVC.AddWorkerWithSpec(name, time, money, notMoney, prof, aaa, spec);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setWorker")
	public void SetWorker(@RequestParam(name="id") Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="time", required=false) Long time, @RequestParam(name="money", required=false) Float money, 
			@RequestParam(name="notmoney", required=false) Float notMoney, @RequestParam(name="prof", required=false) String prof, 
			@RequestParam(name="foto", required=false) String foto, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		workerSVC.SetWorker(id, name, time, money, notMoney, prof, foto, spec);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delWorker")
	public void DelWorker(@RequestParam(name="id") Long id) throws ParseException {
		workerSVC.DelWorker(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workers", produces = {"application/json"})
	public String GetWorkers() {
		JSONObject j = new JSONObject();
		j.put("name", "fff");		
		return j.toString();
		//return workerSVC.GetWorkers();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workersBySpec")
	public List<WorkerDto> GetWorkersBySpec(@RequestParam(name="spec") String spec) {
		return workerSVC.GetWorkersBySpec(spec);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/worker")
	public WorkerDto GetWorker(@RequestParam(name="name") String name) {
		return workerSVC.GetWorker(name);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addClient")
	public void AddClient(@RequestParam(name="email") String email, @RequestParam(name="phone") String num, @RequestParam(name="name") String name, 
			@RequestParam(name="type") String type) throws ParseException {
		clientSVC.AddClient(email, num, name, type);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setClient")
	public void SetClient(@RequestParam(name="id") Long id, @RequestParam(name="email") String email, @RequestParam(name="phone") String num, 
			@RequestParam(name="name") String name, @RequestParam(name="type") String type) throws ParseException {
		clientSVC.SetClient(id, email, num, name, type);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delClient")
	public void DelClient(@RequestParam(name="id") Long id) throws ParseException {
		clientSVC.DelClient(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/updateClient")
	public void UpClient(@RequestParam(name="id") Long id) throws ParseException {
		clientSVC.UpdateType(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clients")
	public List<ClientDto> GetClients() {
		return clientSVC.GetClients();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/client")
	public ClientDto GetClient(@RequestParam(name="name") String name) {
		return clientSVC.GetClient(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clientsByType")
	public List<ClientDto> GetClientsByType(@RequestParam(name="type") String type) {
		return clientSVC.GetClientsByType(type);
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addClientType", produces = {"application/json"})
	public String AddClientType(@RequestParam(name="name", required=true) String name, @RequestParam(name="discount", required=false) Float dis, 
			@RequestParam(name="level", required=true) Long lev, @RequestParam(name="spend", required=false) Float spend, 
			@RequestParam(name="went", required=false) Long went, @RequestParam(name="time", required=false) Long time) throws ParseException {
		int res = clientTypeSVC.AddType(name, dis, lev, spend, went, time);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setClientType", produces = {"application/json"})
	public String SetClientType(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="discount", required=false) Float dis, @RequestParam(name="level", required=false) Long lev, 
			@RequestParam(name="spend", required=false) Float spend, @RequestParam(name="went", required=false) Long went, 
			@RequestParam(name="time", required=false) Long time) throws ParseException {
		int res = clientTypeSVC.SetType(id, name, dis, lev, spend, went, time);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delClientType", produces = {"application/json"})
	public String DelClientType(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = clientTypeSVC.DelType(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clientTypes", produces = {"application/json"})
	public String GetClientTypes() {
		List<ClientTypeDto> aaa = clientTypeSVC.GetClientTypes();
		JSONArray json = new JSONArray();
		for(ClientTypeDto a : aaa) {
			json.put(CreateClientTypeJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clientType", produces = {"application/json"})
	public String GetClientType(@RequestParam(name="name", required=true) String name) {
		ClientTypeDto aaa = clientTypeSVC.GetClientType(name);
		if(aaa != null) {
			JSONObject js = CreateClientTypeJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addProductInCheck")
	public void AddProductInCheck(@RequestParam(name="name") String name, @RequestParam(name="price") Float price, @RequestParam(name="amount") Long amount, 
			@RequestParam(name="check") Long check) throws ParseException {
		productInCheckSVC.AddProduct(name, price, amount, check);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setProductInCheck")
	public void SetProductInCheck(@RequestParam(name="id") Long id, @RequestParam(name="name") String name, @RequestParam(name="price") Float price, 
			@RequestParam(name="amount") Long amount, @RequestParam(name="check") Long check) throws ParseException {
		productInCheckSVC.SetProduct(id, name, price, amount, check);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delProductInCheck")
	public void DelProductInCheck(@RequestParam(name="id") Long id) throws ParseException {
		productInCheckSVC.DelProduct(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productsInCheck")
	public List<ProductInCheckDto> GetProductsInCheck() {
		List<RecordDto> aaa = recordSVC.GetRecords();
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		//return json.toString();
		return productInCheckSVC.GetProducts();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productInCheck")
	public ProductInCheckDto GetProductInCheck(@RequestParam(name="id") Long id) {
		List<RecordDto> aaa = recordSVC.GetRecords();
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		//return json.toString();
		return productInCheckSVC.GetProduct(id);
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addProduct", produces = {"application/json"})
	public String AddProduct(@RequestParam(name="name", required=true) String name, @RequestParam(name="price", required=false) Float price, 
			@RequestParam(name="discountPrice", required=false) Float discountPrice, @RequestParam(name="discountDate", required=false) String discountDate, 
			@RequestParam(name="amount", required=false) Long amount, @RequestParam(name="type", required=false) String type) throws ParseException {
		int res = productSVC.AddProduct(name, price, discountPrice, discountDate, amount, type);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addProductWithType", produces = {"application/json"})
	public String AddProductWithType(@RequestParam(name="name", required=true) String name, @RequestParam(name="price", required=false) Float price, 
			@RequestParam(name="discountPrice", required=false) Float discountPrice, @RequestParam(name="discountDate", required=false) String discountDate, 
			@RequestParam(name="amount", required=false) Long amount, @RequestParam(name="type", required=true) String type) throws ParseException {
		int res = productSVC.AddProductWithType(name, price, discountPrice, discountDate, amount, type);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/updateProducts", produces = {"application/json"})
	public String UpdateProducts() throws ParseException {
		int res = productSVC.UpdateDiscount();
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setProduct", produces = {"application/json"})
	public String SetProduct(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discountPrice", required=false) Float discountPrice, 
			@RequestParam(name="discountDate", required=false) String discountDate, @RequestParam(name="amount", required=false) Long amount, 
			@RequestParam(name="type", required=false) String type) throws ParseException {
		int res = productSVC.SetProduct(id, name, price, discountPrice, discountDate, amount, type);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delProduct", produces = {"application/json"})
	public String DelProduct(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = productSVC.DelProduct(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/products", produces = {"application/json"})
	public String GetProducts() {
		List<ProductDto> aaa = productSVC.GetProducts();
		JSONArray json = new JSONArray();
		for(ProductDto a : aaa) {
			json.put(CreateProductJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/product", produces = {"application/json"})
	public String GetProduct(@RequestParam(name="name", required=true) String name) {
		ProductDto aaa = productSVC.GetProduct(name);
		if(aaa != null) {
			JSONObject js = CreateProductJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", "2");
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productsByType", produces = {"application/json"})
	public String GetProductsByType(@RequestParam(name="name", required=true) String name) {
		List<ProductDto> aaa = productSVC.GetProductsByType(name);
		if(aaa != null) {
			JSONArray json = new JSONArray();
			for(ProductDto a : aaa) {
				json.put(CreateProductJSON(a));
			}
			return json.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
		
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addProductType", produces = {"application/json"})
	public String AddProductType(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = productTypeSVC.AddType(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setProductType", produces = {"application/json"})
	public String SetProductType(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name) throws ParseException {
		int res = productTypeSVC.SetType(id, name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delProductType", produces = {"application/json"})
	public String DelProductType(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = productTypeSVC.DelType(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productTypes", produces = {"application/json"})
	public String GetProductTypes() {
		List<ProductTypeDto> aaa = productTypeSVC.GetTypes();
		JSONArray json = new JSONArray();
		for(ProductTypeDto a : aaa) {
			json.put(CreateProductTypeJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productType", produces = {"application/json"})
	public String GetProductType(@RequestParam(name="name", required=true) String name) {
		ProductTypeDto aaa = productTypeSVC.GetType(name);
		if(aaa != null) {
			JSONObject js = CreateProductTypeJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", "2");
			return js.toString();
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addRecord")
	public void AddRecord(@RequestParam(name="date") String dat, @RequestParam(name="serv") String serv, @RequestParam(name="client") String cli, 
			@RequestParam(name="worker") String wor) throws ParseException {
		recordSVC.AddRecord(dat, serv, cli, wor);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setRecord")
	public void SetRecord(@RequestParam(name="id") Long id, @RequestParam(name="date", required=false) String dat, @RequestParam(name="state") Long state, 
			@RequestParam(name="price") Float price, @RequestParam(name="serv", required=false) String serv, 
			@RequestParam(name="client", required=false) String cli, @RequestParam(name="worker", required=false) String wor) throws ParseException {
		recordSVC.SetRecord(id, dat, state, price, serv, cli, wor);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delRecord")
	public void DelRecord(@RequestParam(name="id") Long id) throws ParseException {
		recordSVC.DelRecord(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/records")
	public String GetRecords() {
		List<RecordDto> aaa = recordSVC.GetRecords();
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/record")
	public String GetRecord(@RequestParam(name="id") Long id) {
		RecordDto aaa = recordSVC.GetRecord(id);
		JSONObject json = CreateRecordJSON(aaa);
		return json.toString();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByWorker")
	public String GetRecordsByWorker(@RequestParam(name="name") String name) {
		List<RecordDto> aaa = recordSVC.GetRecordsByWorker(name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByService")
	public String GetRecordsByService(@RequestParam(name="name") String name) {
		List<RecordDto> aaa = recordSVC.GetRecordsByService(name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByClient")
	public String GetRecordsByClient(@RequestParam(name="name") String name) {
		List<RecordDto> aaa = recordSVC.GetRecordsByClient(name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByDate")
	public String GetRecordsByDate(@RequestParam(name="date1") String p1, @RequestParam(name="date2") String p2) throws ParseException {
		List<RecordDto> aaa = recordSVC.GetRecordsByDate(p1, p2);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByDateAndWorker")
	public String GetRecordsByDateAndWorker(@RequestParam(name="date1") String p1, @RequestParam(name="date2") String p2,
			@RequestParam(name="name") String name) throws ParseException {
		List<RecordDto> aaa = recordSVC.GetRecordsByDateAndWorker(p1, p2, name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/addReview")
	public void AddReview(@RequestParam(name="score") Long score, @RequestParam(name="comment") String comment, @RequestParam(name="client") String client, 
			@RequestParam(name="service") String serv) throws ParseException {
		reviewSVC.AddReview(score, comment, client, serv);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/setReview")
	public void SetReview(@RequestParam(name="id") Long id, @RequestParam(name="score") Long score, @RequestParam(name="comment") String comment, 
			@RequestParam(name="client") String client, @RequestParam(name="service") String serv) throws ParseException {
		reviewSVC.SetReview(id, score, comment, client, serv);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/delReview")
	public void DelReview(@RequestParam(name="id") Long id) throws ParseException {
		reviewSVC.DelReview(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/reviews")
	public List<ReviewDto> GetReviews() throws ParseException {
		return reviewSVC.GetReviews();
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/review")
	public ReviewDto GetReview(@RequestParam(name="id") Long id) throws ParseException {
		return reviewSVC.GetReview(id);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/reviewsByService")
	public List<ReviewDto> GetReviewsByService(@RequestParam(name="name") String name) throws ParseException {
		return reviewSVC.GetReviewsByService(name);
	}
	
	@PutMapping(produces = {"application/json"})
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/reviewsByClient")
	public List<ReviewDto> GetReviewsByClient(@RequestParam(name="name") String name) throws ParseException {
		return reviewSVC.GetReviewsByClient(name);
	}
	
}
