package com.diplom.beautyshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.SpecDto;
import com.diplom.beautyshop.core.WorkerDto;

public interface WorkerRepo extends JpaRepository<WorkerDto, Long>{

	WorkerDto getOneByfioIgnoreCase(String nam);
	
	List<WorkerDto> findAllByspec(SpecDto spec);
}