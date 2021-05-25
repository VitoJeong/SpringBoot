package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// @Repository 생략가능 -> JpaRepository 상속했기때문
public interface UserRepository extends JpaRepository<User, Long>{

	// Spring Data JPA query method
	public User findByUsername(String username);

}
