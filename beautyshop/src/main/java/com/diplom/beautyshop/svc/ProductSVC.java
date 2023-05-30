package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductInCheckDto;
import com.diplom.beautyshop.core.ProductTypeDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ReviewDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.repo.ProductInCheckRepo;
import com.diplom.beautyshop.repo.ProductRepo;
import com.diplom.beautyshop.repo.ProductTypeRepo;
import com.diplom.beautyshop.repo.ServiceRepo;
import com.diplom.beautyshop.repo.ServiceTypeRepo;

import jakarta.transaction.Transactional;

@Service
public class ProductSVC {

	@Autowired
	private ProductTypeRepo productTypes;
	
	@Autowired
	private ProductRepo products;
	
	@Autowired
	private ProductTypeSVC typeSVC;
	
	@Autowired
	private ProductInCheckSVC productInCheckSVC;
	
	@Autowired
	private ProductInCheckRepo productsInCheck;
	
	@Transactional
	public int AddProduct(String name, Float price, Float discountPrice, String discountDate, Long amount, String type) throws ParseException {
		if(products.getOneByNameIgnoreCase(name) != null) return 2;
		ProductTypeDto prodType = null;
		if(type != null) prodType = productTypes.getOneByNameIgnoreCase(type);
		if(prodType == null && type != null) return 2;
		try {
			Date d = null;
			if(discountDate != null) d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(discountDate);
			products.save(new ProductDto(name, price, discountPrice, d, amount, prodType));
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int AddProductWithType(String name, Float price, Float discountPrice, String discountDate, Long amount, String type) 
			throws ParseException {
		int res = typeSVC.AddType(type);
		if(res != 2) {
			res = AddProduct(name, price, discountPrice, discountDate, amount, type);
		}
		return res;
	}
	
	@Transactional
	public int UpdateDiscount() {
		List<ProductDto> prods = products.findAll();
		Date dat = new Date();
		for(ProductDto prod : prods) {
			if(dat.after(prod.discountDate)) {
				prod.discountPrice = null;
				prod.discountDate = null;
				products.save(prod);
			}
		}
		return 1;
	}
	
	@Transactional
	public int SetProduct(Long id, String name, Float price, Float discountPrice, String discountDate, Long amount, String type)
			throws ParseException {
		try {
			ProductDto prod = (ProductDto) Hibernate.unproxy(products.getById(id));
			if(name != null) {
				if(products.getOneByNameIgnoreCase(name) == null) {
					prod.name = name;
				}
				else return 2;
			}
			if(price != null) prod.price = price;
			if(discountPrice != null) prod.discountPrice = discountPrice;
			if(discountDate != null) {
				Date d = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(discountDate);
				prod.discountDate = d;
			}
			if(amount != null) prod.amount = amount;
			if(type != null) {
				ProductTypeDto prodType = productTypes.getOneByNameIgnoreCase(type);
				if(prodType != null) prod.productType = prodType;
				else return 2;
			}
			products.save(prod);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}

	@Transactional
	public int DelProduct(String name) throws ParseException {
		ProductDto prod = products.getOneByNameIgnoreCase(name);
		if(prod != null) {
			List<ProductInCheckDto> prs = productsInCheck.findAllByprod(prod);
			for(ProductInCheckDto pr : prs) {
				productInCheckSVC.DelProduct(pr.pkProductInCheck);
			}
			products.delete(prod);
			return 1;
		}
		else return 2;
	}
	
	public List<ProductDto> GetProducts(){
		return products.findAll();
	}
	
	public ProductDto GetProduct(String name){
		return products.getOneByNameIgnoreCase(name);
	}
	
	public List<ProductDto> GetProductsByType(String type){
		ProductTypeDto prodType = productTypes.getOneByNameIgnoreCase(type);
		if(prodType != null) {
			return products.findAllByproductType(prodType);
		}
		else return null;
	}
	
}
