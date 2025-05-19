package com.example.plusproject.domain.coupon.service;

import com.example.plusproject.domain.coupon.dto.request.CreateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.response.CreateCouponResonseDto;

public interface CouponService {

	CreateCouponResonseDto createCoupon(CreateCouponRequestDto dto);

}
