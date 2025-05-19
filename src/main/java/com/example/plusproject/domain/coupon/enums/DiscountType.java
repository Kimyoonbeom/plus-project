package com.example.plusproject.domain.coupon.enums;

import lombok.Getter;

@Getter
public enum DiscountType {

	VALUE,PERCENT;


	public static DiscountType from(String value){
		return DiscountType.valueOf(value.toUpperCase());
	}
}
