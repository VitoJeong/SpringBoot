package com.dev.aop.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dev.aop.domain.CommonDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect // AOP 적용
@Slf4j
public class BindingAdvice {

	@Around("execution(* com.dev.aop.web..*Controller.*(..))")
	public Object validCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		 String type = joinPoint.getSignature().getDeclaringTypeName();
		 String method = joinPoint.getSignature().getName();
		 
		 log.debug("type :" + type);
		 log.debug(method);
		 
		 Object[] args = joinPoint.getArgs();
		 for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<String, String>();
					
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						log.warn(type + "." + method + "() => 필들 : " + error.getField() + ", 메시지: " + error.getDefaultMessage());
					}
					
					return ResponseEntity
							.status(HttpStatus.BAD_REQUEST)
							.body(new CommonDto<>(errorMap));
				}
			}
		}
		 
		return joinPoint.proceed(); // 함수의 스택을 실행해라
	}
}
