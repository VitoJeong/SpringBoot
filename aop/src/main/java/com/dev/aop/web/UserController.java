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
import com.dev.aop.domain.UserPersistDto;
import com.dev.aop.domain.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
	
	private final UserRepository userRepository;

	// http://localhost:8080/user
	@GetMapping("/user")
	public ResponseEntity<CommonDto<?>> findAll() {
		log.debug("findAll");
		List<User> list = userRepository.findAll();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new CommonDto<>(list.size(), list));
	}

	// http://localhost:8080/user/1
	@GetMapping("/user/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		log.debug("findById: " + id);
		User user = userRepository.findById(id);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new CommonDto<User>(user));
	}
	
	// http://localhost:8080/user
	@PostMapping("/user")
	// x-www-form-urlencoded (request.getParameter())
	// text/plain => @RequestBody
	// application/json => @RequestBody(Object)
	public ResponseEntity<CommonDto<?>> save(@RequestBody UserPersistDto dto) {
		log.debug("save");
		User user = userRepository.save(dto);
		return ResponseEntity
				.ok()
				.body(new CommonDto<>(user));
	}
	
	// http://localhost:8080/user/2
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		log.debug("delete");
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new CommonDto<>(userRepository.delete(id)));
	}
	
	// http://localhost:8080/user/2
	@PutMapping("/user/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserPersistDto dto) {
		log.debug("update : " + dto.toString());
		
		
		User user =userRepository.update(id, dto);
		return ResponseEntity.ok(user);
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public class CommonDto<T> {
		private Integer total;
		private T data;
		
		
		public CommonDto(T data) {
			this.data = data;
		}
	}
}
