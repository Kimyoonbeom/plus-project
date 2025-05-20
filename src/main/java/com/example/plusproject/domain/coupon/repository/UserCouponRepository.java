package com.example.plusproject.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plusproject.domain.coupon.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {
	boolean existsByUser_IdAndCoupon_Id(Long userId, Long couponId);

	long countByCoupon_Id(Long couponId);
}
