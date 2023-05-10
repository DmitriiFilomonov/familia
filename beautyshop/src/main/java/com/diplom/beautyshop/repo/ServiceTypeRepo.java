package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ServiceTypeDto;

public interface ServiceTypeRepo extends JpaRepository<ServiceTypeDto, Long>{

	List<ServiceTypeDto> findByNameIgnoreCase(String nam);
	
	ServiceTypeDto getOneByNameIgnoreCase(String nam);
	
}
