package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService{

	// Security 설정에서  loginProcessingUrl("/login") 설정
	// /login 요청 시 자동으로 UserDetailsService 타입으로 IoC된 loadUserByUsername 실행
	private UserRepository userRepository;
	
	@Autowired
	public PrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Security Session(Authentication(UserDetails))
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 생성됨
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		if(user != null) {
			return new PrincipalDetails(user);
			// Authentication에 저장(일반 로그인)
		}
		return null;
	}

}
