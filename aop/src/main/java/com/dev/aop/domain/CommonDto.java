package com.dev.aop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonDto<T> {
	private Integer total;
	private T data;
	
	
	public CommonDto(T data) {
		this.data = data;
	}
}