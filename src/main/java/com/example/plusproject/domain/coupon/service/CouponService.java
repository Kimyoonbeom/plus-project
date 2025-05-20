package com.example.plusproject.domain.coupon.service;

import com.example.plusproject.domain.coupon.dto.request.CreateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.request.UpdateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.response.CouponInfoResponseDto;
import com.example.plusproject.domain.coupon.dto.response.CreateCouponResonseDto;
import com.example.plusproject.domain.coupon.dto.response.UpdateCouponResponseDto;

public interface CouponService {

	CreateCouponResonseDto createCoupon(CreateCouponRequestDto dto);

	CouponInfoResponseDto couponInfo(Long couponId);

	UpdateCouponResponseDto updateCoupon(Long couponId, UpdateCouponRequestDto dto);

}
