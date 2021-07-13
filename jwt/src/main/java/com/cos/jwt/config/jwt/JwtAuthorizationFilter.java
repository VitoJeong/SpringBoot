package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

// BasicAuthenticationFilter (security)
// 권한이나 인증이 필요한 특정 주소를 요청했을때 필터를 거침
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		super.doFilterInternal(request, response, chain);
		log.info("====== Request BasicAuthenticationFilter");
		
		String jwtHeader = request.getHeader("Authorization");
		log.info("JWT Header : " + jwtHeader);
		
		// 헤더가 있는지 확인
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		// JWT 토큰 검증
		String jwtToken = jwtHeader.replace("Bearer ", "");
		
		String userName = JWT.require(Algorithm.HMAC512("summer"))
				.build()
				.verify(jwtToken)
				.getClaim("username")
				.asString();
		// 서명이 정상적으로 됨
		if(userName != null) {
			User user = userRepository.findByUserName(userName);
			
			log.info(userName);
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			
			// JWT 토큰 서명이 정상이면 Authentication 객체 생성
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// 시큐리티의 세션에 Authentication 객체 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}
		chain.doFilter(request, response);
	}

	
}
