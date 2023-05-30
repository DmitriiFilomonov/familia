package com.diplom.beautyshop.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diplom.beautyshop.core.ClientDto;
import com.diplom.beautyshop.core.RecordDto;
import com.diplom.beautyshop.core.ReviewDto;
import com.diplom.beautyshop.core.ServiceDto;
import com.diplom.beautyshop.core.WorkerDto;
import com.diplom.beautyshop.repo.ClientRepo;
import com.diplom.beautyshop.repo.ReviewRepo;
import com.diplom.beautyshop.repo.ServiceRepo;

import jakarta.transaction.Transactional;

@Service
public class ReviewSVC {

	@Autowired
	private ReviewRepo reviews;
	
	@Autowired
	private ClientRepo clients;
	
	@Autowired
	private ServiceRepo services;
	
	@Transactional
	public void AddReview(Long score, String comment, String client, String serv) throws ParseException {
		ClientDto c = clients.getOneByfioIgnoreCase(client);
		ServiceDto s = services.getOneByNameIgnoreCase(serv);
		reviews.save(new ReviewDto(comment, score, c, s));
	}

	@Transactional
	public void SetReview(Long id, Long score, String comment, String client, String serv) throws ParseException {
		ReviewDto rev = (ReviewDto) Hibernate.unproxy(reviews.getById(id));
		ClientDto c = null;
		if(client != null) c = clients.getOneByfioIgnoreCase(client);
		ServiceDto s = null;
		if(serv != null) s = services.getOneByNameIgnoreCase(serv);
		if(c != null) rev.client = c;
		if(score != null) rev.score = score;
		if(comment != null) rev.comment = comment;
		reviews.save(rev);
	}
	
	@Transactional
	public void DelReview(Long id) {
		ReviewDto rev = reviews.getById(id);
		reviews.delete(rev);
	}
	
	public List<ReviewDto> GetReviews(){
		return reviews.findAll();
	}
	
	public ReviewDto GetReview(Long id){
		return reviews.getById(id);
	}
	
	public List<ReviewDto> GetReviewsByService(String name){
		ServiceDto s = services.getOneByNameIgnoreCase(name);
		List<ReviewDto> r1 = reviews.findAllByservice(s);
		return r1;
	}
	
	public List<ReviewDto> GetReviewsByClient(String name){
		ClientDto c = clients.getOneByfioIgnoreCase(name);
		List<ReviewDto> r1 = reviews.findAllByclient(c);
		return r1;
	}
	
}
