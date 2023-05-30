package com.diplom.beautyshop.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.core.WorkerDto;

public interface ServiceRepo extends JpaRepository<ServiceDto, Long>{
	
	ServiceDto getOneByNameIgnoreCase(String nam);

	Long deleteByNameIgnoreCase(String name);
	
	List<ServiceDto> findAllByserviceType(ServiceTypeDto type);
	
	List<ServiceDto> findAllByspec(SpecDto spec);
}