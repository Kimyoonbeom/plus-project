package com.example.plusproject.domain.coupon.service;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.example.plusproject.domain.coupon.entity.Coupon;
import com.example.plusproject.domain.coupon.enums.DiscountType;
import com.example.plusproject.domain.coupon.repository.CouponRepository;

@SpringBootTest
@ActiveProfiles("test")
@Rollback
class CouponServiceImplTest {

	@Autowired
	private CouponServiceImpl couponServiceImpl;

	@Autowired
	private CouponRepository couponRepository;

	@BeforeEach
	void SetUp() {
		Coupon coupon = Coupon.builder()
			.name("test")
			.discountType(DiscountType.VALUE)
			.discountPrice(100)
			.minOrderPrice(100)
			.maxDiscountPrice(100)
			.couponQuantity(1000L)
			.build();

		couponRepository.save(coupon);
		System.out.println("초기 재고 : " + coupon.getCouponQuantity());  // 초기 Quantity 값 출력
	}

	@Test
	@DisplayName("동시성 이슈 발생")
	void decreaseCouponQuantity() {
		System.out.println("🔅🔅🔅🔅🔅🔅동시성 이슈 발생 메서드 시작🔅🔅🔅🔅🔅🔅");

		IntStream.range(0, 1000).parallel().forEach(i -> {
			Coupon findCoupon = couponRepository.findById(1L).orElseThrow();
			findCoupon.decreaseCouponQuantity();
			couponRepository.save(findCoupon);
		});

		Coupon findCouponAgain = couponRepository.findById(1L).orElseThrow();
		System.out.println("최종 재고 : " + findCouponAgain.getCouponQuantity());
	}

	@Test
	@DisplayName("비관적 락을 이용한 동시성 제어")
	void decreaseCouponQuantityWithPessimisticLock() {
		System.out.println("🔅🔅🔅🔅🔅🔅비관적 락을 이용한 동시성 제어 시작🔅🔅🔅🔅🔅🔅");

		IntStream.range(0, 1000).parallel().forEach(i -> {
			couponServiceImpl.decreaseCouponQuantityWithPessimisticLock(1L);
		});

		Coupon findCouponAgain = couponRepository.findById(1L).orElseThrow();
		System.out.println("최종 재고 : " + findCouponAgain.getCouponQuantity());
	}

}