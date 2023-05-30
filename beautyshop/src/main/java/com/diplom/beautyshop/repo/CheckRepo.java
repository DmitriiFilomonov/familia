package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.CheckDto;
import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ProductDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.WorkerDto;

public interface CheckRepo extends JpaRepository<CheckDto, Long>{

	List<CheckDto> findAllByworker(WorkerDto worker);
	
	List<CheckDto> findAllByclient(ClientDto client);
	
}
