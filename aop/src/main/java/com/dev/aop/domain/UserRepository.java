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
	
	public Long save(User user) {
		log.debug("INSERT INTO user VALUES (.....)");
		
		return 22l;
	}
}
