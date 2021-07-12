package com.cos.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cos.jwt.filter.MyFilterA;
import com.cos.jwt.filter.MyFilterB;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<MyFilterA> filterA() {
		FilterRegistrationBean<MyFilterA> bean = new FilterRegistrationBean<>(new MyFilterA());
		bean.addUrlPatterns("/*");
		bean.setOrder(0);
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<MyFilterB> filterB() {
		FilterRegistrationBean<MyFilterB> bean = new FilterRegistrationBean<>(new MyFilterB());
		bean.addUrlPatterns("/*");
		bean.setOrder(1);
		return bean;
	}
}
