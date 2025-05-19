package com.example.plusproject.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plusproject.domain.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
}
