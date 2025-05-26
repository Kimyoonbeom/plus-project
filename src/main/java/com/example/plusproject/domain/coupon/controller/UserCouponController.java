package com.example.plusproject.domain.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.coupon.dto.response.UserCouponResponseDto;
import com.example.plusproject.domain.coupon.service.UserCouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserCouponController {

	private final UserCouponService userCouponService;

	@PostMapping("/coupons/{couponId}/issue")
	public ResponseEntity<UserCouponResponseDto> issueCoupon (
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long couponId){

		UserCouponResponseDto userCouponResponseDto = userCouponService.issueCoupon(authUser.getId(), couponId);

		return new ResponseEntity<>(userCouponResponseDto, HttpStatus.CREATED);
	}

	@GetMapping("/users/{userId}/coupons")
	public ResponseEntity<Page<UserCouponResponseDto>> getUserCoupons(
		@PathVariable Long userId,
		Pageable pageable) {
		Page<UserCouponResponseDto> coupons = userCouponService.getUserCoupons(userId, pageable);
		return ResponseEntity.ok(coupons);
	}
}
