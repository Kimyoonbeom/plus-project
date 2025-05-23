package com.example.plusproject.domain.coupon.dto.request;

import java.time.LocalDateTime;

import com.example.plusproject.domain.coupon.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCouponRequestDto {

	private final String name;

	private final DiscountType discountType;

	private final int discountPrice;

	private final int minOrderPrice;

	private final int maxDiscountPrice;

	private final boolean duplicatePossible;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime couponStartDay;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime couponEndDay;

	private final Long couponQuantity;

}
