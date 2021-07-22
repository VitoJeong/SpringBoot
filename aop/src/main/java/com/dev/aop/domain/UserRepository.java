package com.dev.aop.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserRepository {
  
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		users.add(new User(1l, "nike", "1234", "0100000"));
		users.add(new User(2l, "peaches", "1234", "0100000"));
		users.add(new User(3l, "dev", "1234", "0100000"));
		
		return users;
	}
	
	public User findById(Long id){
		return new User(1l, "nike", "1234", "0100000");
	}
	
	public User save(UserPersistDto dto) {
		log.debug("INSERT INTO user VALUES (.....)");
		User user = new User(22L, 
				dto.getUserName(), 
				dto.getPassword(), 
				dto.getPhone());
		return user;
	}
	
	public User update(Long id, UserPersistDto dto) {
		
		return new User(id, 
				dto.getUserName(), 
				dto.getPassword(), 
				dto.getPhone());
	}
	
	public boolean delete(Long id) {
		
		if (id > 10) throw new IllegalArgumentException("Handler");
		
		return true;
	}
}
