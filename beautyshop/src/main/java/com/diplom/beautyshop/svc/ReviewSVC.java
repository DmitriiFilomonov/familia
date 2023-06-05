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
	public int AddReview(Long score, String comment, String client, String serv) throws ParseException {
		ClientDto c = null;
		if(client != null) {
			c = clients.getOneByfioIgnoreCase(client);
			if(c == null) return 2;
		}
		ServiceDto s = null;
		if(serv != null) {
			s = services.getOneByNameIgnoreCase(serv);
			if(s == null) return 2;
		}
		reviews.save(new ReviewDto(comment, score, c, s));
		return 1;
	}

	@Transactional
	public int SetReview(Long id, Long score, String comment, String client, String serv) throws ParseException {
		try {
			ReviewDto rev = (ReviewDto) Hibernate.unproxy(reviews.getById(id));
			if(rev == null) return 2;
			ClientDto c = null;
			if(client != null) {
				c = clients.getOneByfioIgnoreCase(client);
				if(c == null) return 2;
			}
			ServiceDto s = null;
			if(serv != null) {
				s = services.getOneByNameIgnoreCase(serv);
				if(s == null) return 2;
			}
			if(c != null) rev.client = c;
			if(s != null) rev.service = s;
			if(score != null) rev.score = score;
			if(comment != null) rev.comment = comment;
			reviews.save(rev);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
	}
	
	@Transactional
	public int DelReview(Long id) {
		try {
			ReviewDto rev = reviews.getById(id);
			if(rev == null) return 2;
			reviews.delete(rev);
			return 1;
		}
		catch(Exception ex) {
			return 2;
		}
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
