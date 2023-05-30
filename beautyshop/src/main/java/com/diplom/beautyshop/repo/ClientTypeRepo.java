package com.diplom.beautyshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.ClientTypeDto;
import com.diplom.beautyshop.core.ServiceDto;

public interface ClientTypeRepo extends JpaRepository<ClientTypeDto, Long>{

	ClientTypeDto getOneByNameIgnoreCase(String nam);
	
	ClientTypeDto getOneBylevel(Long lev);
	
}
