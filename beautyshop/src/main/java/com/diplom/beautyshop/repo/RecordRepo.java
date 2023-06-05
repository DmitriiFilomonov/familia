package com.diplom.beautyshop.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.ServiceTypeDto;
import com.diplom.beautyshop.core.WorkerDto;

public interface RecordRepo extends JpaRepository<RecordDto, Long>{
	
	List<RecordDto> findAllByservice(ServiceDto serv);
	
	List<RecordDto> findAllByworker(WorkerDto worker);
	
	List<RecordDto> findAllByclient(ClientDto client);
	
	@Query("select r1 from RecordDto r1 "
			+ "where r1.date>=:p1 and r1.date<=:p2")
	List<RecordDto> findAllByDate(@Param("p1") Date p1, @Param("p2") Date p2);
	
	@Query("select r1 from RecordDto r1 "
			+ "join WorkerDto w on w.pkWorker=r1.worker "
			+ "where r1.date>=:p1 and r1.date<=:p2 and w.fio=:nam")
	List<RecordDto> findAllByDateAndWorker(@Param("p1") Date p1, @Param("p2") Date p2, @Param("nam") String name);
	
	@Query(value = "select r1.pk_record, r1.client, r1.service, r1.worker, r1.date, r1.price, r1.state from record r1 "
			+ "join worker w on w.pk_worker=r1.worker "
			+ "join service s on s.pk_service=r1.service "
			+ "where r1.date<=:p1 and (r1.date + s.length * interval '1 minute')>=:p1 and w.fio=:nam", nativeQuery = true)
	List<RecordDto> checkFreeTime(@Param("p1") Date p1, @Param("nam") String name);
}
