package com.example.plusproject.domain.coupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.coupon.dto.request.CreateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.response.CouponInfoResponseDto;
import com.example.plusproject.domain.coupon.dto.response.CreateCouponResonseDto;
import com.example.plusproject.domain.coupon.service.CouponService;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.enums.UserRole;
import com.example.plusproject.domain.user.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;
	private final UserRepository userRepository;

	// security 수정하면 @AuthenticationPrincipal AuthUser authUser랑
	// 유저 권한 확인 로직 필요없음!
	@PostMapping("/admin/coupons")
	public ResponseEntity<CreateCouponResonseDto> createCoupon(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody CreateCouponRequestDto request){
		System.out.println(authUser.getUserRole());

		User user = userRepository.findById(authUser.getId())
			.orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저입니다."));

		if (!user.getUserRole().equals(UserRole.ADMIN)) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		CreateCouponResonseDto coupon = couponService.createCoupon(request);
		return new ResponseEntity<>(coupon, HttpStatus.CREATED);
	}

	@GetMapping("/admin/coupons/{couponId}")
	public ResponseEntity<CouponInfoResponseDto> couponInfo(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long couponId){
		CouponInfoResponseDto responseDto = couponService.couponInfo(couponId);
		return new ResponseEntity<>(responseDto,HttpStatus.OK);
	}

}
