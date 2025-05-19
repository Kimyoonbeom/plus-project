package com.example.plusproject.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plusproject.domain.user.dto.request.LoginRequest;
import com.example.plusproject.domain.user.dto.request.SignUpRequest;
import com.example.plusproject.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request){
		return new ResponseEntity<>(userService.signup(request), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request){
		return new ResponseEntity<>(userService.login(request),HttpStatus.OK);
	}
}
