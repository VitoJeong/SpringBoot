package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 서버가 응답할 때 JSON을 자바스크립트에서 처리할 수 있도록 설정
		config.addAllowedOrigin("*"); // 모든 URL에 요청을 허용
		config.addAllowedHeader("*"); // 모든 header에 요청을 허용
		config.addAllowedMethod("*"); // 모든 http method 요청에 허용
		source.registerCorsConfiguration("/api/**", config);
		
		return new CorsFilter(source);
	}
}
