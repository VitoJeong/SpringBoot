package com.cos.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.model.User;
import com.cos.instagram.repository.FollowRepository;
import com.cos.instagram.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {
	
	
	private BCryptPasswordEncoder encoder;
	
	private UserRepository userRepository;
	
	private FollowRepository followRepository;
	
	public UserController(BCryptPasswordEncoder encoder, UserRepository userRepository,
			FollowRepository followRepository) {
		this.encoder = encoder;
		this.userRepository = userRepository;
		this.followRepository = followRepository;
	}

	@GetMapping("/auth/login")
	public String authLogin() {
		return "auth/login";
	}
	
	@GetMapping("/auth/join")
	public String authJoin() {
		return "auth/join";
	}
	
	@PostMapping("/auth/joinProc")
	public String authJoinProc(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		// com.cos.instagram.controller : debug
		log.debug("rawPassword : " + rawPassword);
		log.debug("encPassword : " + encPassword);
		
		userRepository.save(user);
		
		return "redirect:/auth/login";
	}
	
	@GetMapping("/user/{id}")
	public String profile(@PathVariable Long id) {
		
		/**
		 *  1. imageCount
		 *  2. followerCount
		 *  3. followingCoung
		 *  4. User Object (Image(likeCount) 컬렉션)
		 *  5. followCheck
		 */		
		
		
		return "user/profile";
	}
	
}
