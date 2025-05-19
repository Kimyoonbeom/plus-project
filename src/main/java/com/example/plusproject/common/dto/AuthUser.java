package com.example.plusproject.common.dto;

import com.example.plusproject.domain.user.enums.UserRole;

import lombok.Getter;

@Getter
public class AuthUser {

	private final Long id;
	private final String email;
	private final UserRole userRole;
	private final String nickName;

	public AuthUser(Long id, String email, UserRole userRole, String nickName) {
		this.id = id;
		this.email = email;
		this.userRole = userRole;
		this.nickName = nickName;
	}
}
