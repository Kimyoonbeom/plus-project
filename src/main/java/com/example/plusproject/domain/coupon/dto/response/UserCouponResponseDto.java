package com.example.plusproject.domain.coupon.dto.response;

import java.time.LocalDateTime;

import com.example.plusproject.domain.coupon.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCouponResponseDto {

	private final Long userCouponId;
	private final String couponName;
	private final DiscountType discountType;
	private final boolean used;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime issuedAt;

}
