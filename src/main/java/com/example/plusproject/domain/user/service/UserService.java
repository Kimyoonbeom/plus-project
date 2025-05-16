package com.example.plusproject.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.plusproject.config.JwtUtil;
import com.example.plusproject.domain.user.dto.request.LoginRequest;
import com.example.plusproject.domain.user.dto.request.SignUpRequest;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.enums.UserRole;
import com.example.plusproject.domain.user.repository.UserRepository;
import com.sun.jdi.request.InvalidRequestStateException;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public String signup(@Valid SignUpRequest request) {
		if(userRepository.existsByEmail(request.getEmail())){
			throw new InvalidRequestStateException("이미 존재하는 이메일 입니다.");
		}

		UserRole userRole = UserRole.valueOf(request.getUserRole());

		User newUser = new User(
			userRole,
			request.getEmail(),
			request.getPassword(),
			request.getNickName()
		);

		userRepository.save(newUser);
		// User savedUser = userRepository.save(newUser);

		// String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole,
		// 	savedUser.getNickname());

		return "회원가입 완료";
	}

	public String login(@Valid LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new InvalidRequestStateException("존재하지 않는 유저입니다."));

		if(!user.getPassword().equals(request.getPassword())){
			throw new RuntimeException("비밀번호를 재확인해주세요.");
		}
		String bearerToken = jwtUtil.createToken(user.getId(),user.getEmail(),user.getUserRole(),user.getNickname());

		return "로그인 성공 : " + bearerToken;
	}
}
