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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import io.jsonwebtoken.Jwts;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServlet;

@Controller
@RequestMapping(value = "/app")
public class AppControl {
	
	@Autowired
	private HttpServlet request;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private JwtTokenProvider tokenProv; 

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
	
	
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addCheck", produces = {"application/json"})
	public String AddCheck(@RequestParam(name="date", required=true) String date, @RequestParam(name="state", required=true) Long state, 
			@RequestParam(name="discount", required=false) Float dis, @RequestParam(name="client", required=true) String client, 
			@RequestParam(name="worker", required=true) String worker) throws ParseException {
		int res = checkSVC.AddCheck(date, state, dis, client, worker);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setCheck", produces = {"application/json"})
	public String SetCheck(@RequestParam(name="id", required=true) Long id, @RequestParam(name="date", required=false) String date, 
			@RequestParam(name="state", required=false) Long state, @RequestParam(name="discount", required=false) Float dis, 
			@RequestParam(name="client", required=false) String client, @RequestParam(name="worker", required=false) String worker) throws ParseException {
		int res = checkSVC.SetCheck(id, date, state, dis, client, worker);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delCheck", produces = {"application/json"})
	public String DelCheck(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = checkSVC.DelCheck(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/checks", produces = {"application/json"})
	public String GetChecks() {
		List<CheckDto> aaa =  checkSVC.GetChecks();
		JSONArray json = new JSONArray();
		for(CheckDto a : aaa) {
			json.put(CreateCheckJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/checksByClient", produces = {"application/json"})
	public String GetChecksByClient(@RequestParam(name="name", required=true) String name) {
		List<CheckDto> aaa = checkSVC.GetChecksByClient(name);
		JSONArray json = new JSONArray();
		for(CheckDto a : aaa) {
			json.put(CreateCheckJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/checksByWorker", produces = {"application/json"})
	public String GetChecksByWorker(@RequestParam(name="name", required=true) String name) {
		List<CheckDto> aaa = checkSVC.GetChecksByWorker(name);
		JSONArray json = new JSONArray();
		for(CheckDto a : aaa) {
			json.put(CreateCheckJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/check", produces = {"application/json"})
	public String GetCheck(@RequestParam(name="id", required=true) Long id) {
		CheckDto aaa = checkSVC.GetCheck(id);
		if(aaa != null) {
			JSONObject js = CreateCheckJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addServ", produces = {"application/json"})
	public String AddService(@RequestParam(name="name", required=true) String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		int res = serviceSVC.AddService(name, time, price, discount, dat, sex, type, spec);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addServWithType", produces = {"application/json"})
	public String AddServiceWithType(@RequestParam(name="name", required=true) String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		int res = serviceSVC.AddServiceWithType(name, time, price, discount, dat, sex, type, spec);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addServWithSpec", produces = {"application/json"})
	public String AddServiceWithSpec(@RequestParam(name="name", required=true) String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="discount", required=false) Float discount, 
			@RequestParam(name="discount_end", required=false) String dat, @RequestParam(name="sex", required=false) String sex, 
			@RequestParam(name="type", required=false) String type, @RequestParam(name="spec", required=false) String spec) throws ParseException {
		int res = serviceSVC.AddServiceWithSpec(name, time, price, discount, dat, sex, type, spec);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setServ", produces = {"application/json"})
	public String SetService(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="time", required=false) Long time, @RequestParam(name="price", required=false) Float price, 
			@RequestParam(name="discount", required=false) Float discount, @RequestParam(name="date", required=false) String dat, 
			@RequestParam(name="sex", required=false) String sex, @RequestParam(name="type", required=false) String type,
			@RequestParam(name="spec", required=false) String spec) throws ParseException {
		int res = serviceSVC.SetService(id, name, time, price, discount, dat, sex, type, spec);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delServ", produces = {"application/json"})
	public String DelService(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = serviceSVC.DelService(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/updateServices", produces = {"application/json"})
	public String UpdateServices() throws ParseException {
		int res = serviceSVC.UpdateService();
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
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
		if(cli.clientType != null) {
			j.put("pkClientType", cli.clientType.pkClientType);
			j.put("name", cli.clientType.name);
			js.put("type", j);
		}
		else {
			js.put("type", "null");
		}
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
		j = new JSONObject();
		j.put("pkWorker", rec.worker.pkWorker);
		j.put("fio", rec.worker.fio);
		js.put("worker", j);
		j = new JSONObject();
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
		JSONObject j1 = new JSONObject();
		if(serv.spec != null) {
			j1.put("pkSpec", serv.spec.pkSpec);
			j1.put("name", serv.spec.name);
			js.put("spec", j1);
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
			j = new JSONObject();
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
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servs", produces = {"application/json"})
	public String GetServices(@RequestParam(name="f", required=true) Long f) {
		List<ServiceDto> aaa = serviceSVC.GetServices(f);
		JSONArray json = new JSONArray();
		for(ServiceDto a : aaa) {
			json.put(CreateServiceJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsBySpec", produces = {"application/json"})
	public String GetServicesBySpec(@RequestParam(name="spec", required=true) String spec, @RequestParam(name="f", required=true) Long f) {
		List<ServiceDto> aaa = serviceSVC.GetServicesBySpec(spec, f);
		JSONArray json = new JSONArray();
		for(ServiceDto a : aaa) {
			json.put(CreateServiceJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsBySpecId", produces = {"application/json"})
	public String GetServicesBySpecId(@RequestParam(name="id", required=true) Long spec, @RequestParam(name="f", required=true) Long f) {
		List<ServiceDto> aaa = serviceSVC.GetServicesBySpec(spec, f);
		JSONArray json = new JSONArray();
		for(ServiceDto a : aaa) {
			json.put(CreateServiceJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsSortedById", produces = {"application/json"})
	public String GetServicesSortedById() {
		List<ServiceDto> aaa = serviceSVC.GetServicesSortedById();
		JSONArray json = new JSONArray();
		for(ServiceDto a : aaa) {
			json.put(CreateServiceJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsByType", produces = {"application/json"})
	public String GetServicesByType(@RequestParam(name="type", required=true) String type, @RequestParam(name="f", required=true) Long f) {
		List<ServiceDto> aaa = serviceSVC.GetServicesByType(type, f);
		JSONArray json = new JSONArray();
		for(ServiceDto a : aaa) {
			json.put(CreateServiceJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servsByTypeId", produces = {"application/json"})
	public String GetServicesByTypeId(@RequestParam(name="id", required=true) Long type, @RequestParam(name="f", required=true) Long f) {
		List<ServiceDto> aaa = serviceSVC.GetServicesByType(type, f);
		JSONArray json = new JSONArray();
		for(ServiceDto a : aaa) {
			json.put(CreateServiceJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/serv", produces = {"application/json"})
	public String GetService(@RequestParam(name="name", required=true) String name) {
		ServiceDto aaa = serviceSVC.GetService(name);
		if(aaa != null) {
			JSONObject js = CreateServiceJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servId", produces = {"application/json"})
	public String GetServiceId(@RequestParam(name="id", required=true) Long name) {
		ServiceDto aaa = serviceSVC.GetService(name);
		if(aaa != null) {
			JSONObject js = CreateServiceJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addSpec", produces = {"application/json"})
	public String AddSpec(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = specSVC.AddSpec(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setSpec", produces = {"application/json"})
	public String SetSpec(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=true) String name) throws ParseException {
		int res = specSVC.SetSpec(id, name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delSpec", produces = {"application/json"})
	public String DelSpec(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = specSVC.DelSpec(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/specs", produces = {"application/json"})
	public String GetSpecs() {
		List<SpecDto> aaa = specSVC.GetSpecs();
		JSONArray json = new JSONArray();
		for(SpecDto a : aaa) {
			json.put(CreateSpecJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/spec", produces = {"application/json"})
	public String GetSpec(@RequestParam(name="name") String name) {
		SpecDto aaa = specSVC.GetSpec(name);
		if(aaa != null) {
			JSONObject js = CreateSpecJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addServType", produces = {"application/json"})
	public String AddServiceType(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = serviceTypeSVC.AddType(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setServType", produces = {"application/json"})
	public String SetServiceType(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name) throws ParseException {
		int res = serviceTypeSVC.SetType(id, name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delServType", produces = {"application/json"})
	public String DelServiceType(@RequestParam(name="name", required=true) String name) throws ParseException {
		int res = serviceTypeSVC.DelType(name);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servTypes", produces = {"application/json"})
	public String GetServiceTypes() {
		List<ServiceTypeDto> aaa = serviceTypeSVC.GetTypes();
		JSONArray json = new JSONArray();
		for(ServiceTypeDto a : aaa) {
			json.put(CreateServiceTypeJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/servType", produces = {"application/json"})
	public String GetServiceType(@RequestParam(name="name", required=true) String name) {
		ServiceTypeDto aaa = serviceTypeSVC.GetType(name);
		if(aaa != null) {
			JSONObject js = CreateServiceTypeJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addWorker", produces = {"application/json"})
	public String AddWorker(@RequestParam(name="name", required=true) String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="money", required=false) Float money, @RequestParam(name="notmoney", required=false) Float notMoney, 
			@RequestParam(name="prof", required=false) String prof, @RequestParam(name="foto", required=false) MultipartFile foto, 
			@RequestParam(name="login", required=false) String login, @RequestParam(name="pass", required=false) String pass,
			@RequestParam(name="spec", required=false) String spec) throws ParseException, IllegalStateException, IOException {
	    String aaa = foto.getOriginalFilename();
		String encodedPass = encoder.encode(pass);
		int res = workerSVC.AddWorker(name, time, money, notMoney, prof, aaa,login,encodedPass, spec);
		if(res == 1) {
			File dir = new File("/workersImg");
			if(!dir.exists()) {
				dir.mkdir();
			}
		    foto.transferTo(new File("/workersImg/" + aaa));
		}
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addWorkerWithSpec", produces = {"application/json"})
	public String AddWorkerWithSpec(@RequestParam(name="name", required=true) String name, @RequestParam(name="time", required=false) Long time, 
			@RequestParam(name="money", required=false) Float money, @RequestParam(name="notmoney", required=false) Float notMoney, 
			@RequestParam(name="prof", required=false) String prof, @RequestParam(name="foto", required=false) MultipartFile foto, 
			@RequestParam(name="login", required=false) String login, @RequestParam(name="pass", required=false) String pass,
			@RequestParam(name="spec", required=false) String spec) throws ParseException, IllegalStateException, IOException {
	    String aaa = foto.getOriginalFilename();
		int res = workerSVC.AddWorkerWithSpec(name, time, money, notMoney, prof, aaa, login, pass, spec);
		if(res == 1) {
			File dir = new File("/workersImg");
			if(!dir.exists()) {
				dir.mkdir();
			}
		    foto.transferTo(new File("/workersImg/" + aaa));
		}
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setWorker", produces = {"application/json"})
	public String SetWorker(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="time", required=false) Long time, @RequestParam(name="money", required=false) Float money, 
			@RequestParam(name="notmoney", required=false) Float notMoney, @RequestParam(name="prof", required=false) String prof, 
			@RequestParam(name="login", required=false) String login, @RequestParam(name="pass", required=false) String pass,
			@RequestParam(name="foto", required=false) MultipartFile foto, @RequestParam(name="spec", required=false) String spec) throws ParseException,
	IllegalStateException, IOException {
		String aaa = null;
		if(foto != null) aaa = foto.getOriginalFilename();
		String lastFoto = workerSVC.GetWorker(id).foto;
		int res = workerSVC.SetWorker(id, name, time, money, notMoney, prof, aaa, login, pass, spec);
		if(res == 1) {
			File dir = new File("/workersImg");
			if(!dir.exists()) {
				dir.mkdir();
			}
			new File("/workersImg/" + lastFoto).delete();
		    foto.transferTo(new File("/workersImg/" + aaa));
		}
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delWorker", produces = {"application/json"})
	public String DelWorker(@RequestParam(name="id", required=true) Long id) throws ParseException {
		String lastFoto = workerSVC.GetWorker(id).foto;
		int res = workerSVC.DelWorker(id);
		if(res == 1) {
			new File("/workersImg/" + lastFoto).delete();
		}
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workers", produces = {"application/json"})
	public String GetWorkers(@RequestParam(name="f") Long f) {
		List<WorkerDto> aaa = workerSVC.GetWorkers(f);
		JSONArray json = new JSONArray();
		for(WorkerDto a : aaa) {
			json.put(CreateWorkerJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workersBySpec", produces = {"application/json"})
	public String GetWorkersBySpec(@RequestParam(name="spec") String spec, @RequestParam(name="f") Long f) {
		List<WorkerDto> aaa = workerSVC.GetWorkersBySpec(spec, f);
		JSONArray json = new JSONArray();
		for(WorkerDto a : aaa) {
			json.put(CreateWorkerJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workersBySpecId", produces = {"application/json"})
	public String GetWorkersBySpecId(@RequestParam(name="id") Long spec, @RequestParam(name="f") Long f) {
		List<WorkerDto> aaa = workerSVC.GetWorkersBySpec(spec, f);
		JSONArray json = new JSONArray();
		for(WorkerDto a : aaa) {
			json.put(CreateWorkerJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/worker", produces = {"application/json"})
	public String GetWorker(@RequestParam(name="name") String name) {
		WorkerDto aaa = workerSVC.GetWorker(name);
		if(aaa != null) {
				//if(tokenProv.getUsername(token).equals(aaa.login)) {
					JSONObject js = CreateWorkerJSON(aaa);
					return js.toString();
				//}
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/workerId", produces = {"application/json"})
	public String GetWorkerId(@RequestParam(name="id") Long name) {
		WorkerDto aaa = workerSVC.GetWorker(name);
		if(aaa != null) {
			JSONObject js = CreateWorkerJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addClient", produces = {"application/json"})
	public String AddClient(@RequestParam(name="email", required=false) String email, @RequestParam(name="phone", required=false) String num, 
			@RequestParam(name="name", required=true) String name, @RequestParam(name="type", required=false) String type) throws ParseException {
		int res = clientSVC.AddClient(email, num, name, type);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setClient", produces = {"application/json"})
	public String SetClient(@RequestParam(name="id", required=true) Long id, @RequestParam(name="email", required=false) String email, 
			@RequestParam(name="phone", required=false) String num, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="type", required=false) String type) throws ParseException {
		int res = clientSVC.SetClient(id, email, num, name, type);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delClient", produces = {"application/json"})
	public String DelClient(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = clientSVC.DelClient(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/updateClient", produces = {"application/json"})
	public String UpClient(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = clientSVC.UpdateType(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clients", produces = {"application/json"})
	public String GetClients() {
		List<ClientDto> aaa = clientSVC.GetClients();
		JSONArray json = new JSONArray();
		for(ClientDto a : aaa) {
			json.put(CreateClientJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/client", produces = {"application/json"})
	public String GetClient(@RequestParam(name="name", required=true) String name) {
		ClientDto aaa = clientSVC.GetClient(name);
		if(aaa != null) {
			JSONObject js = CreateClientJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/clientsByType", produces = {"application/json"})
	public String GetClientsByType(@RequestParam(name="type", required=true) String type) {
		List<ClientDto> aaa = clientSVC.GetClientsByType(type);
		JSONArray json = new JSONArray();
		for(ClientDto a : aaa) {
			json.put(CreateClientJSON(a));
		}
		return json.toString();
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
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addProductInCheck", produces = {"application/json"})
	public String AddProductInCheck(@RequestParam(name="name", required=true) String name, 
			@RequestParam(name="amount", required=true) Long amount, @RequestParam(name="check", required=true) Long check) throws ParseException {
		int res = productInCheckSVC.AddProduct(name, amount, check);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setProductInCheck", produces = {"application/json"})
	public String SetProductInCheck(@RequestParam(name="id", required=true) Long id, @RequestParam(name="name", required=false) String name, 
			@RequestParam(name="price", required=false) Float price, @RequestParam(name="amount", required=false) Long amount, 
			@RequestParam(name="check", required=false) Long check) throws ParseException {
		int res = productInCheckSVC.SetProduct(id, name, price, amount, check);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/updateProductInCheck", produces = {"application/json"})
	public String UpdateProductInCheck(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = productInCheckSVC.UpdatePrice(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delProductInCheck", produces = {"application/json"})
	public String DelProductInCheck(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = productInCheckSVC.DelProduct(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productsInCheck", produces = {"application/json"})
	public String GetProductsInCheck() {
		List<ProductInCheckDto> aaa = productInCheckSVC.GetProducts();
		JSONArray json = new JSONArray();
		for(ProductInCheckDto a : aaa) {
			json.put(CreateProductInCheckJSON(a));
		}
		return json.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productsInCheckByCheck", produces = {"application/json"})
	public String GetProductsInCheckByCheck(@RequestParam(name="id", required=true) Long id) {
		List<ProductInCheckDto> aaa = productInCheckSVC.GetProductsByCheck(id);
		JSONArray json = new JSONArray();
		for(ProductInCheckDto a : aaa) {
			json.put(CreateProductInCheckJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/productInCheck", produces = {"application/json"})
	public String GetProductInCheck(@RequestParam(name="id", required=true) Long id) {
		ProductInCheckDto aaa = productInCheckSVC.GetProduct(id);
		if(aaa != null) {
			JSONObject js = CreateProductInCheckJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", 2);
			return js.toString();
		}
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
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addRecord", produces = {"application/json"})
	public String AddRecord(@RequestParam(name="date", required=true) String dat, @RequestParam(name="serv", required=true) String serv, 
			@RequestParam(name="client", required=true) String cli, @RequestParam(name="worker", required=true) String wor) throws ParseException {
		int res = recordSVC.AddRecord(dat, serv, cli, wor);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/updateRecord", produces = {"application/json"})
	public String UpdateRecord(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = recordSVC.UpdatePrice(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setRecord", produces = {"application/json"})
	public String SetRecord(@RequestParam(name="id", required=true) Long id, @RequestParam(name="date", required=false) String dat, 
			@RequestParam(name="state", required=false) Long state, @RequestParam(name="price", required=false) Float price, 
			@RequestParam(name="serv", required=false) String serv, @RequestParam(name="client", required=false) String cli, 
			@RequestParam(name="worker", required=false) String wor) throws ParseException {
		int res = recordSVC.SetRecord(id, dat, state, price, serv, cli, wor);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delRecord", produces = {"application/json"})
	public String DelRecord(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = recordSVC.DelRecord(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/records", produces = {"application/json"})
	public String GetRecords() {
		List<RecordDto> aaa = recordSVC.GetRecords();
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/record", produces = {"application/json"})
	public String GetRecord(@RequestParam(name="id", required=true) Long id) {
		RecordDto aaa = (RecordDto) Hibernate.unproxy(recordSVC.GetRecord(id));
		if(aaa != null) {
			JSONObject js = CreateRecordJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", "2");
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByWorker", produces = {"application/json"})
	public String GetRecordsByWorker(@RequestParam(name="name", required=true) String name) {
		List<RecordDto> aaa = recordSVC.GetRecordsByWorker(name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByService", produces = {"application/json"})
	public String GetRecordsByService(@RequestParam(name="name", required=true) String name) {
		List<RecordDto> aaa = recordSVC.GetRecordsByService(name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByClient", produces = {"application/json"})
	public String GetRecordsByClient(@RequestParam(name="name", required=true) String name) {
		List<RecordDto> aaa = recordSVC.GetRecordsByClient(name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByDate", produces = {"application/json"})
	public String GetRecordsByDate(@RequestParam(name="date1", required=true) String p1, @RequestParam(name="date2", required=true) String p2) 
			throws ParseException {
		List<RecordDto> aaa = recordSVC.GetRecordsByDate(p1, p2);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/recordsByDateAndWorker", produces = {"application/json"})
	public String GetRecordsByDateAndWorker(@RequestParam(name="date1", required=true) String p1, @RequestParam(name="date2", required=true) String p2,
			@RequestParam(name="name", required=true) String name) throws ParseException {
		List<RecordDto> aaa = recordSVC.GetRecordsByDateAndWorker(p1, p2, name);
		JSONArray json = new JSONArray();
		for(RecordDto a : aaa) {
			json.put(CreateRecordJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/getWorkerFreeTime", produces = {"application/json"})
	public String GetWorkerFreeTime(@RequestParam(name="date", required=true) String p1, 
			@RequestParam(name="name", required=true) String name) throws ParseException {
		List<Date> aaa = recordSVC.GetFreeTime(p1, name);
		JSONArray json = new JSONArray();
		for(Date a : aaa) {
			json.put(a.getHours() + ":" + a.getMinutes());
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/addReview", produces = {"application/json"})
	public String AddReview(@RequestParam(name="score", required=true) Long score, @RequestParam(name="comment", required=false) String comment, 
			@RequestParam(name="client", required=true) String client, @RequestParam(name="service", required=true) String serv) throws ParseException {
		int res = reviewSVC.AddReview(score, comment, client, serv);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/setReview", produces = {"application/json"})
	public String SetReview(@RequestParam(name="id", required=true) Long id, @RequestParam(name="score", required=false) Long score, 
			@RequestParam(name="comment", required=false) String comment, @RequestParam(name="client", required=false) String client, 
			@RequestParam(name="service", required=false) String serv) throws ParseException {
		int res = reviewSVC.SetReview(id, score, comment, client, serv);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/delReview", produces = {"application/json"})
	public String DelReview(@RequestParam(name="id", required=true) Long id) throws ParseException {
		int res = reviewSVC.DelReview(id);
		JSONObject js = new JSONObject();
		js.put("response", res);
		return js.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/reviews", produces = {"application/json"})
	public String GetReviews() throws ParseException {
		List<ReviewDto> aaa = reviewSVC.GetReviews();
		JSONArray json = new JSONArray();
		for(ReviewDto a : aaa) {
			json.put(CreateReviewJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/review", produces = {"application/json"})
	public String GetReview(@RequestParam(name="id", required=true) Long id) throws ParseException {
		ReviewDto aaa = reviewSVC.GetReview(id);
		if(aaa != null) {
			JSONObject js = CreateReviewJSON(aaa);
			return js.toString();
		}
		else {
			JSONObject js = new JSONObject();
			js.put("response", "2");
			return js.toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/reviewsByService", produces = {"application/json"})
	public String GetReviewsByService(@RequestParam(name="name", required=true) String name) throws ParseException {
		List<ReviewDto> aaa = reviewSVC.GetReviewsByService(name);
		JSONArray json = new JSONArray();
		for(ReviewDto a : aaa) {
			json.put(CreateReviewJSON(a));
		}
		return json.toString();
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value = "/reviewsByClient", produces = {"application/json"})
	public String GetReviewsByClient(@RequestParam(name="name", required=true) String name) throws ParseException {
		List<ReviewDto> aaa = reviewSVC.GetReviewsByClient(name);
		JSONArray json = new JSONArray();
		for(ReviewDto a : aaa) {
			json.put(CreateReviewJSON(a));
		}
		return json.toString();
	}
	
}
