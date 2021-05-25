package com.cos.security1.auth;

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
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}

}
