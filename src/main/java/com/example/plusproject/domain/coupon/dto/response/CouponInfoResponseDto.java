package com.example.plusproject.domain.coupon.dto.response;

import java.time.LocalDateTime;

import com.example.plusproject.domain.coupon.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponInfoResponseDto {

	private Long id;

	private String name;

	private DiscountType discountType;

	private int discountPrice;

	private int minOrderPrice;

	private int maxDiscountPrice;

	private boolean duplicatePossible;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime couponStartDay;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime couponEndDay;

	private Long couponQuantityIssued;

	private boolean status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime deletedAt;

}
