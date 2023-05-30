package com.diplom.beautyshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.SpecDto;

public interface SpecRepo extends JpaRepository<SpecDto, Long>{

	SpecDto getOneByNameIgnoreCase(String nam);
	
}
