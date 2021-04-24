package com.cos.instagram.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.instagram.model.User;
import com.cos.instagram.repository.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{

	private UserRepository userRepository;
	
	public MyUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUserName(username);
		
		MyUserDetail userDetail = null;
		
		if(user != null) {
			userDetail = new MyUserDetail();
			userDetail.setUser(user);
		} else {
			throw new UsernameNotFoundException("Not Found 'username'");
		}
		
		return userDetail;
	}

	
}
