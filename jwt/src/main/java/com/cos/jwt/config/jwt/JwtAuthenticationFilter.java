package com.cos.jwt.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// UsernamePasswordAuthenticationFilter -> 로그인 요청시 동작하는 필터
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authencitManager;

	// 로그인 시도를 위해 실행
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("로그인 시도 : JwtAuthenticationFilter");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(request.getInputStream(), User.class);
			log.info(user.toString());
			
			// 1. 로그인을 시도한 userName, password 담고 있음(AuthenticationFilter에 의해 생성)
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

			// 2. 로그인 시도 
			// 3. 로그인 후 PrincipalDetailsService 호출(loadUserByUsername실행)
			Authentication authentication = 
					authencitManager.authenticate(authenticationToken);
			
			// 4. authentication 객체가 session에 저장 -> 로그인 완료
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			log.info(principalDetails.getUser().getUserName());
			
			// 리턴하면 authentication객체가 session에 저장
			// 권한 관리를 security 대신하게 만듬
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	// attemptAuthentication()에서 인증이 되면 successfulAuthentication 실행
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("=============successful Authentication");
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
		.withSubject("summer_token")
		.withExpiresAt(new Date(System.currentTimeMillis() + 30*60*1000))
		.withClaim(SPRING_SECURITY_FORM_USERNAME_KEY, principalDetails.getUser().getUserName())
		.withClaim("id", principalDetails.getUser().getId())
		.sign(Algorithm.HMAC512("summer"));
		
		response.addHeader("Authorization", "Bearer " + jwtToken);
	}
	
	
}
