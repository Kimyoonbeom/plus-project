package com.example.plusproject.domain.coupon.entity;

import java.time.LocalDateTime;

import com.example.plusproject.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private String discountType;

	@Column(nullable = false)
	private int discountPrice;

	@Column(nullable = false)
	private int minOrderPrice;

	@Column(nullable = false)
	private int maxDiscountPrice;

	private LocalDateTime couponStartDay;

	private LocalDateTime couponEndDay;

	@Column(nullable = false)
	private int couponQuantityIssued;

	@Column(nullable = false)
	private boolean status = true;

	private LocalDateTime deletedAt;

}
