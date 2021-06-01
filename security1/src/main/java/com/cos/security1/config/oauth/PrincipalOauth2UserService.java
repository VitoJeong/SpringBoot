package com.cos.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.NaverUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private UserRepository userRepository;
	
	@Autowired
	public PrincipalOauth2UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
	}


	// Google로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 생성됨
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("ClientRegistration =" + userRequest.getClientRegistration());
		log.info("AccessToken =" + userRequest.getAccessToken());
		
		// 로그인 -> code리턴(OAuth-Client라이브러리) -> AccessToken요청
		// userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아옴.
		OAuth2User oauth2User = super.loadUser(userRequest);
		Map<String, Object> attributes = oauth2User.getAttributes();
		log.info("Attributes =" +  attributes);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(registrationId.equals("google")) 
		{
			log.info("Google Log-In");
			oAuth2UserInfo = new GoogleUserInfo(attributes);
		} 
		else if(registrationId.equals("facebook")) 
		{
			log.info("Facebook Log-In");
			oAuth2UserInfo = new FacebookUserInfo(attributes);
		} 
		else if(registrationId.equals("naver")) 
		{
			log.info("Naver Log-In");
			oAuth2UserInfo = new NaverUserInfo(attributes);
		} 
		else 
		{
			log.warn("소셜 로그인은 구글, 페이스북, 네이버로 이용 가능합니다.");
		}
		
		String provider = oAuth2UserInfo.getProvider(); // google
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode("GetInThere");
		String email = oAuth2UserInfo.getEmail(); 
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) 
		{
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
		// Authentication에 저장(OAuth 로그인)
		
	}

	
}
