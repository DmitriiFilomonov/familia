package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ClientTypeDto;

public interface ClientRepo extends JpaRepository<ClientDto, Long>{

	ClientDto getOneByfioIgnoreCase(String nam);
	
	List<ClientDto> findAllByclientType(ClientTypeDto tp);
}
