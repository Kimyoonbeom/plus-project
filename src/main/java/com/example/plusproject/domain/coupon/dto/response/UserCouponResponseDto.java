package com.example.plusproject.domain.coupon.dto.response;

import java.time.LocalDateTime;

import com.example.plusproject.domain.coupon.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCouponResponseDto {

	private Long userCouponId;
	private String couponName;
	private DiscountType discountType;
	private boolean used;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime issuedAt;

}
