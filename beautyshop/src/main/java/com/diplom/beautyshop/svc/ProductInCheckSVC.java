package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductInCheckDto;
import com.diplom.beautyshop.core.ProductTypeDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.repo.CheckRepo;
import com.diplom.beautyshop.repo.ProductInCheckRepo;
import com.diplom.beautyshop.repo.ProductRepo;
import com.diplom.beautyshop.repo.ProductTypeRepo;

import jakarta.transaction.Transactional;

@Service
public class ProductInCheckSVC {

	@Autowired
	private ProductInCheckRepo productsInCheck;
	
	@Autowired
	private ProductRepo products;
	
	@Autowired
	private CheckRepo checks;
	
	@Autowired
	private ClientSVC clientSVC;
	
	@Transactional
	public int AddProduct(String name, Long amount, Long check) throws ParseException {
		ProductDto prod = null;
		if(name != null) {
			prod = products.getOneByNameIgnoreCase(name);
			if(prod == null) return 2;
		}
		CheckDto ch = null;
		if(check != null) {
			try {
				ch = checks.getById(check);
				if(ch == null) return 2;
				if(ch.state != 1) return 2;
			}
			catch(Exception ex) {
				return 2;
			}
		}
		Float price;
		if(ch.client.clientType != null) price = prod.price * (1 - prod.discountPrice - ch.client.clientType.discount);
		else price = prod.price * (1 - prod.discountPrice);
		productsInCheck.save(new ProductInCheckDto(prod, amount, price, ch));
		return 1;
	}
	
	@Transactional
	public int UpdatePrice(Long id) {
		try {
			ProductInCheckDto rec = (ProductInCheckDto) Hibernate.unproxy(productsInCheck.getById(id));
			if(rec == null) return 2;
			Float pr;
			if(rec.check.client.clientType != null) pr = rec.prod.price * (1 - rec.prod.discountPrice - rec.check.client.clientType.discount);
			else pr = rec.prod.price * (1 - rec.prod.discountPrice);
			rec.price = pr;
			productsInCheck.save(rec);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int SetProduct(Long id, String name, Float price, Long amount, Long check)
			throws ParseException {
		try {
			ProductInCheckDto prodInCheck = (ProductInCheckDto) Hibernate.unproxy(productsInCheck.getById(id));
			if(prodInCheck == null) return 2;
			ProductDto prod = null;
			CheckDto ch = null;
			if(name != null) {
				prod = products.getOneByNameIgnoreCase(name);
				if(prod == null) return 2;
			}
			if(check != null) {
				try {
					ch = checks.getById(check);
					if(ch == null) return 2;
					if(ch.state != 1) return 2;
				}
				catch(Exception ex) {
					return 2;
				}
			}
			if(price != null) prodInCheck.price = price;
			if(amount != null) prodInCheck.amount = amount;
			if(prod != null) prodInCheck.prod = prod;
			if(ch != null) prodInCheck.check = ch;
			if(price == null && (prod != null || ch != null)) UpdatePrice(prodInCheck.pkProductInCheck);
			productsInCheck.save(prodInCheck);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelProduct(Long id) throws ParseException {
		try {
			ProductInCheckDto prod = productsInCheck.getById(id);
			if(prod == null) return 2;
			productsInCheck.delete(prod);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	public List<ProductInCheckDto> GetProducts(){
		return productsInCheck.findAll();
	}
	
	public ProductInCheckDto GetProduct(Long id){
		return productsInCheck.getById(id);
	}
	
	public List<ProductInCheckDto> GetProductsByCheck(Long id){
		CheckDto ch = checks.getById(id);
		return productsInCheck.findAllBycheck(ch);
	}
	
}
