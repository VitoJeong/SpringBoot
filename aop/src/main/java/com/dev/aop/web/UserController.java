package com.dev.aop.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.aop.domain.User;
import com.dev.aop.domain.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
	
	private final UserRepository userRepository;

	// http://localhost:8080/user
	@GetMapping("/user")
	public Response findAll() {
		log.debug("findAll");
		return new Response(userRepository.findAll());
	}

	// http://localhost:8080/user/1
	@GetMapping("/user/{id}")
	public Response findById(@PathVariable Long id) {
		log.debug("findById: " + id);
		return new Response(userRepository.findById(id));
	}
	
	// http://localhost:8080/user
	@PostMapping("/user")
	// x-www-form-urlencoded (request.getParameter())
	// text/plain => @RequestBody
	// application/json => @RequestBody(Object)
	public ResponseEntity<Long> save(@RequestBody User user) {
		log.debug("save");
		log.debug("username: " + user.getUserName());
		log.debug("password: " + user.getPassword());
		log.debug("phone: " + user.getPhone());
		
		return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
	}
	
	// http://localhost:8080/user/2
	@DeleteMapping("/user/{id}")
	public void delete(@PathVariable Long id) {
		log.debug("delete");
	}
	
	// http://localhost:8080/user/2
	@PutMapping("/user/{id}")
	public void update(String password, String phone) {
		log.debug("update");
	}
	
	@Getter
	public class Response {
		private Object data;

		public Response(Object data) {
			this.data = data;
		}
		
	}
}
