package com.cos.instagram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.instagram.model.Likes;
import com.cos.instagram.model.User;
import com.cos.instagram.repository.LikesRepository;
import com.cos.instagram.service.MyUserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LikesController {
	
	private LikesRepository mLikesRepository;

	@Autowired
	public LikesController(LikesRepository mLikesRepository) {
		this.mLikesRepository = mLikesRepository;
	}
	
	@GetMapping("/like/notification")
	public List<Likes> likeNotification
	(
		@AuthenticationPrincipal MyUserDetail userDetail
	)
	{
		User principal = userDetail.getUser();
		log.info(principal.getId().toString());
		List<Likes> likesList = mLikesRepository.findLikeNotification(principal.getId());
		return likesList;
	}

}
