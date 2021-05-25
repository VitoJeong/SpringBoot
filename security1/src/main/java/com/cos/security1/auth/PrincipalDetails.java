package com.cos.security1.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

public class PrincipalDetails implements UserDetails{

	// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
	// 로그인 진행이 완료되면 시큐리티 session을 만들어줌 (Security ContextHolder)
	// Session 정보 -> Authentication type 객체
	// Authentication 내에 User 정보가 있어야 함
	// User Type => UserDetails 타입 객체
		
	// Security Session => Authentication => UserDetails
	
	// 콤포지션
	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// User의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		
		
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	

	
}
