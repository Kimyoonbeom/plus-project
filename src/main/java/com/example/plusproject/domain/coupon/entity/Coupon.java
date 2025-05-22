package com.example.plusproject.domain.coupon.entity;

import java.time.LocalDateTime;

import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.domain.coupon.dto.request.UpdateCouponRequestDto;
import com.example.plusproject.domain.coupon.enums.DiscountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Coupon extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private DiscountType discountType;

	@Column(nullable = false)
	private int discountPrice;

	@Column(nullable = false)
	private int minOrderPrice;

	@Column(nullable = false)
	private int maxDiscountPrice;

	@Column(nullable = false)
	private boolean duplicatePossible = false;

	private LocalDateTime couponStartDay;

	private LocalDateTime couponEndDay;

	@Column(nullable = false)
	private Long couponQuantity; // 발행된 쿠폰 수량 -> couponQuantity 다 수정

	// coupon ->
	// id = 1 -> 2000원 할인 쿠폰, 5개(couponQuantity)
	// id = 2 -> 30% 할인 쿠폰

	// userCoupon ->
	// id = 1 -> 1번 유저의 2000원 할인 쿠폰
	// id = 2 -> 2번 유저의 2000원 할인 쿠폰
	// id = 3 -> 2번 유저의 2000원 할인 쿠폰
	// id = 4 -> 2번 유저의 2000원 할인 쿠폰
	// id = 5 -> 2번 유저의 2000원 할인 쿠폰
	// id = 6 -> 1번 유저의 30% 할인 쿠폰

	@Column(nullable = false)
	private boolean status = false;

	private LocalDateTime deletedAt;

	@Builder
	public Coupon(String name, DiscountType discountType, int discountPrice, int minOrderPrice, int maxDiscountPrice,
		boolean duplicatePossible, LocalDateTime couponStartDay, LocalDateTime couponEndDay, Long couponQuantity,
		LocalDateTime deletedAt) {
		this.name = name;
		this.discountType = discountType;
		this.discountPrice = discountPrice;
		this.minOrderPrice = minOrderPrice;
		this.maxDiscountPrice = maxDiscountPrice;
		this.duplicatePossible = duplicatePossible;
		this.couponStartDay = couponStartDay;
		this.couponEndDay = couponEndDay;
		this.couponQuantity = couponQuantity;
		this.deletedAt = deletedAt;
	}

	public void updateCoupon(UpdateCouponRequestDto dto){
		this.name = dto.getName();
		this.discountType = dto.getDiscountType();
		this.discountPrice = dto.getDiscountPrice();
		this.minOrderPrice = dto.getMinOrderPrice();
		this.maxDiscountPrice = dto.getMaxDiscountPrice();
		this.duplicatePossible = dto.isDuplicatePossible();
		this.couponStartDay = dto.getCouponStartDay();
		this.couponEndDay = dto.getCouponEndDay();
		this.couponQuantity = dto.getCouponQuantity();
	}

	public void deleteCoupon(){
		this.status = true;
		this.deletedAt = LocalDateTime.now();
	}
}
