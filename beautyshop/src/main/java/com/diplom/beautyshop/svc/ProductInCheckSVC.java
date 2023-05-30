package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductInCheckDto;
import com.diplom.beautyshop.core.ProductTypeDto;
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
	public void AddProduct(String name, Float price, Long amount, Long check) throws ParseException {
		ProductDto prod = null;
		if(name != null) prod = products.getOneByNameIgnoreCase(name);
		CheckDto ch = null;
		if(check != null) ch = checks.getById(check);
		productsInCheck.save(new ProductInCheckDto(prod, amount, price, ch));
	}
	
	@Transactional
	public void SetProduct(Long id, String name, Float price, Long amount, Long check)
			throws ParseException {
		ProductInCheckDto prodInCheck = productsInCheck.getById(id);
		if(name != null) {
			ProductDto prod = products.getOneByNameIgnoreCase(name);
			prodInCheck.prod = prod;
		}
		if(price != null) prodInCheck.price = price;
		if(amount != null) prodInCheck.amount = amount;
		if(check != null) {
			CheckDto ch = checks.getById(check);
			prodInCheck.check = ch;
		}
		productsInCheck.save(prodInCheck);
		clientSVC.UpdateType(prodInCheck.check.client.pkClient);
	}
	
	@Transactional
	public void DelProduct(Long id) throws ParseException {
		ProductInCheckDto prod = productsInCheck.getById(id);
		productsInCheck.delete(prod);
	}
	
	public List<ProductInCheckDto> GetProducts(){
		return productsInCheck.findAll();
	}
	
	public ProductInCheckDto GetProduct(Long id){
		return productsInCheck.getById(id);
	}
	
}
