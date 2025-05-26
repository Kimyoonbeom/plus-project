package com.example.plusproject.domain.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.plusproject.domain.coupon.dto.response.UserCouponResponseDto;

public interface UserCouponService {

	UserCouponResponseDto issueCoupon(Long userId,Long couponId);

	Page<UserCouponResponseDto> getUserCoupons(Long userId, Pageable pageable);
}
