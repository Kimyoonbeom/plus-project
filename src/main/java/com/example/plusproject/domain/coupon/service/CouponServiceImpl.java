package com.example.plusproject.domain.coupon.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.plusproject.domain.coupon.dto.request.CreateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.response.CouponInfoResponseDto;
import com.example.plusproject.domain.coupon.dto.response.CreateCouponResonseDto;
import com.example.plusproject.domain.coupon.entity.Coupon;
import com.example.plusproject.domain.coupon.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

	private final CouponRepository couponRepository;

	@Override
	public CreateCouponResonseDto createCoupon(CreateCouponRequestDto dto) {

		Coupon coupon = Coupon.builder()
			.name(dto.getName())
			.discountType(dto.getDiscountType())
			.discountPrice(dto.getDiscountPrice())
			.minOrderPrice(dto.getMinOrderPrice())
			.maxDiscountPrice(dto.getMaxDiscountPrice())
			.duplicatePossible(dto.isDuplicatePossible())
			.couponStartDay(dto.getCouponStartDay())
			.couponEndDay(dto.getCouponEndDay())
			.couponQuantityIssued(dto.getCouponQuantityIssued())
			.build();

		Coupon save = couponRepository.save(coupon);

		return new CreateCouponResonseDto(
			save.getId(),
			save.getName(),
			save.getDiscountType(),
			save.getDiscountPrice(),
			save.getMinOrderPrice(),
			save.getMaxDiscountPrice(),
			save.isDuplicatePossible(),
			save.getCouponStartDay(),
			save.getCouponEndDay(),
			save.getCouponQuantityIssued(),
			save.isStatus(),
			save.getCreatedAt()
			);
	}

	@Override
	public CouponInfoResponseDto couponInfo(Long couponId) {

		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(()-> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

		return new CouponInfoResponseDto(
			coupon.getId(),
			coupon.getName(),
			coupon.getDiscountType(),
			coupon.getDiscountPrice(),
			coupon.getMinOrderPrice(),
			coupon.getMaxDiscountPrice(),
			coupon.isDuplicatePossible(),
			coupon.getCouponStartDay(),
			coupon.getCouponEndDay(),
			coupon.getCouponQuantityIssued(),
			coupon.isStatus(),
			coupon.getDeletedAt()
		);
	}
}
