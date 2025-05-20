package com.example.plusproject.domain.coupon.service;

import org.springframework.stereotype.Service;

import com.example.plusproject.domain.coupon.dto.request.CreateCouponRequestDto;
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
}
