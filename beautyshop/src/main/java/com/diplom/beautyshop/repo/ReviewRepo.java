package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ReviewDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.WorkerDto;

public interface ReviewRepo extends JpaRepository<ReviewDto, Long>{

	List<ReviewDto> findAllByservice(ServiceDto serv);
	
	List<ReviewDto> findAllByclient(ClientDto client);
	
}
