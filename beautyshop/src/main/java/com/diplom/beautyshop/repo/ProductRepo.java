package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductTypeDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;

public interface ProductRepo extends JpaRepository<ProductDto, Long>{

	ProductDto getOneByNameIgnoreCase(String nam);
	
	List<ProductDto> findAllByproductType(ProductTypeDto type);
	
}
