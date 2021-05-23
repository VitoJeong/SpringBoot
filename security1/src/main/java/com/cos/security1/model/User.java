package com.cos.security1.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class User {

	@Id @GeneratedValue
	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String role;
	
	@CreationTimestamp
	private Timestamp createDate;
	
}
