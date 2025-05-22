package com.example.plusproject.domain.coupon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.plusproject.domain.coupon.dto.request.CreateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.request.UpdateCouponRequestDto;
import com.example.plusproject.domain.coupon.dto.response.CouponInfoResponseDto;
import com.example.plusproject.domain.coupon.dto.response.CreateCouponResonseDto;
import com.example.plusproject.domain.coupon.dto.response.UpdateCouponResponseDto;
import com.example.plusproject.domain.coupon.entity.Coupon;
import com.example.plusproject.domain.coupon.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

	private final CouponRepository couponRepository;

	@Transactional
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
			.couponQuantity(dto.getCouponQuantity())
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
			save.getCouponQuantity(),
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
			coupon.getCouponQuantity(),
			coupon.isStatus(),
			coupon.getDeletedAt()
		);
	}

	@Transactional
	@Override
	public UpdateCouponResponseDto updateCoupon(Long couponId, UpdateCouponRequestDto dto) {

		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(()->new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

		coupon.updateCoupon(dto);

		return new UpdateCouponResponseDto(
			coupon.getId(),
			coupon.getName(),
			coupon.getDiscountType(),
			coupon.getDiscountPrice(),
			coupon.getMinOrderPrice(),
			coupon.getMaxDiscountPrice(),
			coupon.isDuplicatePossible(),
			coupon.getCouponStartDay(),
			coupon.getCouponEndDay(),
			coupon.getCouponQuantity(),
			coupon.isStatus(),
			coupon.getCreatedAt(),
			coupon.getUpdatedAt()
		);
	}

	@Transactional
	@Override
	public void deleteCoupon(Long couponId) {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

		coupon.deleteCoupon();

	}

	/**
	 * 쿠폰 재고 감소 로직 - 비관적 락 적용
	 * @param couponId
	 */
	@Transactional
	public void decreaseCouponQuantityWithPessimisticLock(Long couponId) {
		Coupon findCoupon = couponRepository.findByIdWithPessimisticLockOrElseThrow(couponId);
		findCoupon.decreaseCouponQuantity();
		couponRepository.save(findCoupon);
	}
}
