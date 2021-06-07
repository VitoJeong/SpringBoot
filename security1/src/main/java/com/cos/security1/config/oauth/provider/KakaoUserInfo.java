package com.cos.security1.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;
	private Map<String, Object> properties;
	private String id;
	
	public KakaoUserInfo(Map<String, Object> attributes) {
		this.id = String.valueOf(attributes.get("id"));
		this.attributes = (Map<String, Object>) attributes.get("kakao_account");
		this.properties = (Map<String, Object>) attributes.get("properties");
	}

	@Override
	public String getProviderId() {
		return id;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) properties.get("nickname");
	}

	
}
