package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // Spring Security 필터가 스프링필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true,
							prePostEnabled = true) // secure annotation, preAuthorize 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private PrincipalOauth2UserService oauth2UserService;
	
	@Bean // 메서드의 리턴 오브젝트를 컨테이너에 등록
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 처리
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm") 
			.userInfoEndpoint()
			.userService(oauth2UserService);// 구글 로그인이 완료된 후 처리가 필요함. -> 코드 X (액세스토큰 + 프로필정보)
		
			// 1.코드받기(인증) 2. 엑세스토큰(권한) 3. 사용자프로필 정보를 가져옴
			//4. 정보를 토대로 회원가입 자동 진행 -> 정보가 모자랄 경우 회원가입 요구
			
	}

	
	
}
