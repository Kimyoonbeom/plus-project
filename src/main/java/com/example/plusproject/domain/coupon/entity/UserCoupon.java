package com.example.plusproject.domain.coupon.entity;

import java.sql.Blob;
import java.time.LocalDateTime;

import com.example.plusproject.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id",nullable = false)
	private Coupon coupon;

	private boolean used = false;

	private LocalDateTime issuedAt;

	private LocalDateTime usedAt;

	public UserCoupon(User user, Coupon coupon, boolean used, LocalDateTime issuedAt) {
		this.user = user;
		this.coupon = coupon;
		this.used = used;
		this.issuedAt = LocalDateTime.now();
	}
}
