package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductTypeDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.repo.ProductRepo;
import com.diplom.beautyshop.repo.ProductTypeRepo;
import com.diplom.beautyshop.repo.ServiceTypeRepo;

import jakarta.transaction.Transactional;

@Service
public class ProductTypeSVC {

	@Autowired
	private ProductTypeRepo types;
	
	@Autowired
	private ProductRepo products;
	
	@Transactional
	public int AddType(String name) {
		ProductTypeDto type = new ProductTypeDto(name);
		if(types.getOneByNameIgnoreCase(name) == null) {
			types.save(type);
			return 1;
		}
		else return 2;
	}

	@Transactional
	public int SetType(Long id, String name) {
		try {
			ProductTypeDto type = (ProductTypeDto) Hibernate.unproxy(types.getOne(id));
			if(type != null) {
				if(name != null) {
					if(types.getOneByNameIgnoreCase(name) == null) {
						type.name = name;
						types.save(type);
						return 1;
					}
				}
			}
			return 2;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelType(String name) throws ParseException {
		ProductTypeDto type = types.getOneByNameIgnoreCase(name);
		if(type != null) {
			List<ProductDto> prods = products.findAllByproductType(type);
			for(ProductDto pr : prods) {
				pr.productType = null;
			}
			types.delete(type);
			return 1;
		}
		else return 2;
	}
	
	public List<ProductTypeDto> GetTypes(){
		return types.findAll();
	}
	
	public ProductTypeDto GetType(String name){
		return types.getOneByNameIgnoreCase(name);
	}
	
}
