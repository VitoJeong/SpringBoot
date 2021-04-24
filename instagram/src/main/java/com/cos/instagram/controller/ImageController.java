package com.cos.instagram.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.instagram.service.MyUserDetail;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ImageController {

	@GetMapping({"/","/image/feed"})
	public String imageFeed(@AuthenticationPrincipal MyUserDetail userDetail) {
		
		log.info("userName : " + userDetail.getUsername());
		return "image/feed";
	}
	
}
