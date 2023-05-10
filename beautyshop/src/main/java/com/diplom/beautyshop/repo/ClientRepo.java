package com.diplom.beautyshop.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diplom.beautyshop.core.ClientDto;

public interface ClientRepo extends JpaRepository<ClientDto, Long>{

	ClientDto getOneByfioIgnoreCase(String nam);
}
