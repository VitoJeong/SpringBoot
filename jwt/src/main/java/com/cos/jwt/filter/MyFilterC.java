package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyFilterC implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// 정상적으로 로그인 후 토큰을 반환
		// 요청시 header에 Authorization에 value값으로 토큰을 가져옴
		// 그때 넘어온 토큰을 검증(RSA, HS256)
		// 토큰 : summer
		if(req.getMethod().equals("POST")) {
			String headerAuth = req.getHeader("Authorization");
			log.info(req.getRequestURI());
			log.info(headerAuth);
			if(req.getRequestURI().equals("/join")) chain.doFilter(req, res);
			if(headerAuth.equals("summer")) {
				chain.doFilter(req, res); // 설정하지 않으면 프로세스 진행안됨
			} else {
				PrintWriter out = res.getWriter();
				out.println("Not Authorization");
			}
		}
		
		log.info("필터 C");
	}

}
