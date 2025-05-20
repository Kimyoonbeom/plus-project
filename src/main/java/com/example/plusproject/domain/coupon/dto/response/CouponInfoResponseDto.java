package com.example.plusproject.domain.coupon.dto.response;

import java.time.LocalDateTime;

import com.example.plusproject.domain.coupon.enums.DiscountType;

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

	private LocalDateTime couponStartDay;

	private LocalDateTime couponEndDay;

	private Long couponQuantityIssued;

	private boolean status;

	private LocalDateTime deletedAt;

}
