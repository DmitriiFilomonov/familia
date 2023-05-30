package com.diplom.beautyshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ProductTypeDto;
import com.diplom.beautyshop.core.ServiceTypeDto;

public interface ProductTypeRepo extends JpaRepository<ProductTypeDto, Long>{

	ProductTypeDto getOneByNameIgnoreCase(String nam);
	
}
