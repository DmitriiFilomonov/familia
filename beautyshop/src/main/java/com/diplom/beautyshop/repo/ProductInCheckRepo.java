package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.ProductInCheckDto;

public interface ProductInCheckRepo extends JpaRepository<ProductInCheckDto, Long>{

	List<ProductInCheckDto> findAllBycheck(CheckDto check);
	
	List<ProductInCheckDto> findAllByprod(ProductDto prod);
	
}
