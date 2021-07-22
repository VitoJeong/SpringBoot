package com.dev.aop.domain;

import lombok.Data;

@Data
public class UserPersistDto {
	private String userName;
	private String password;
	private String phone;
}
