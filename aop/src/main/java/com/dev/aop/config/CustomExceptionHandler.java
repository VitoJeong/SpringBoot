package com.dev.aop.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice // 모든 예외를 관리
public class CustomExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public String aaa(Exception e) {
		
		return e.toString();
		
	}
}
