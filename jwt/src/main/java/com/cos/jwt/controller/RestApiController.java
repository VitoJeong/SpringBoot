package com.cos.jwt.controller;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestApiController {

	private final BCryptPasswordEncoder encoder;
	
	private final UserRepository userRepository;

	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@PostMapping("token")
	public String token() {
		return "token";
	}
	
	@PostMapping("join")
	public String join(@RequestBody UserToJoin userToJoin) {
		
		String userName = userToJoin.getUserName();
		String password = encoder.encode(userToJoin.getPassword());
		if(Objects.nonNull(userRepository.findByUserName(userName))) throw new IllegalArgumentException("userName");
		
		
		log.info("========" + password);
		User user = new User();
		user.setUserName(userToJoin.getUserName());
		user.setPassword(password);
		user.setRoles("ROLE_USER");

		userRepository.save(user);
		
		return user.toString();
	}
	
	@Getter
	static class UserToJoin {
		String userName;
		String password;
	}
}
