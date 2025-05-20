package com.example.plusproject.domain.coupon.service;

import com.example.plusproject.domain.coupon.dto.response.UserCouponResponseDto;

public interface UserCouponService {

	UserCouponResponseDto issueCoupon(Long userId,Long couponId);

}
