package com.cos.security1.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

	@Id @GeneratedValue
	private Long id;
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	private String email;
	
	private String role;
	
	private String provider;
	
	private String providerId;
	
	@CreationTimestamp
	private Timestamp createDate;

	@Builder
	public User( String username, String password, String email, String role, String provider,
			String providerId) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
	}
	
	
}
