package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {
	
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public IndexController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin
	(
		Authentication authentication,
		@AuthenticationPrincipal PrincipalDetails userDetails
	) 
	{
		log.info("/test/login ============");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		log.info("authentication = " + principalDetails.getUser());
		log.info("userDetails = " + userDetails.getUser());
		return principalDetails.getUser().toString();
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin
	(
		Authentication authentication,
		@AuthenticationPrincipal OAuth2User oauth
	) 
	{
		log.info("/test/login ============");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		log.info("authentication = " + oAuth2User.getAttributes());
		log.info("oAuth2User = " + oauth.getAttributes());
		return oauth.getAttributes().toString();
	}
	
	// localhost:8080/
	// localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// ???????????? Default src/main/resources/
		// View Resolver : templates(prefix), mustache (suffix)
		return "index";
		// src/main/resources/templates/index.mustache
	}

	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info("principalDetails = " + principalDetails.getUser());
		return principalDetails.getUser().toString();
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	// Spring security??? ????????? ????????? - SecurityConfig ?????? ?????? ??? ????????????
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	
	@PostMapping("/join")
	public String join(User user) {
		log.info("user = " + user);
		user.setRole("ROLE_USER");
		
		// ??????????????? ??????????????? ????????? ??????????????? ???????????? ????????????!
		String rawPassword = user.getPassword();
		String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);
		
		userRepository.save(user);
		
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "User Info";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "User Data";
	}
	
}
