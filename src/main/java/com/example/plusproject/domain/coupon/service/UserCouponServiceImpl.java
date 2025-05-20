package com.example.plusproject.domain.coupon.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.plusproject.domain.coupon.dto.response.UserCouponResponseDto;
import com.example.plusproject.domain.coupon.entity.Coupon;
import com.example.plusproject.domain.coupon.entity.UserCoupon;
import com.example.plusproject.domain.coupon.repository.CouponRepository;
import com.example.plusproject.domain.coupon.repository.UserCouponRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

	private final UserRepository userRepository;
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;

	@Override
	public UserCouponResponseDto issueCoupon(Long userId, Long couponId) {

		User user = userRepository.findById(userId)
			.orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저입니다."));

		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(()-> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

		//삭제 체크
		if (coupon.isStatus()){
			throw new IllegalArgumentException("삭제된 쿠폰입니다.");
		}

		// 생각해보니 만료일 체크 null 이면 만료x
		if (coupon.getCouponEndDay() != null && coupon.getCouponEndDay().isBefore(LocalDateTime.now())){
			throw new IllegalArgumentException("만료된 쿠폰입니다.");
		}

		// 수량제한있으면 수량제한도 체크해야됨

		long issuedCouponCount = userCouponRepository.countByCoupon_Id(couponId);
		Long maxCoupon = coupon.getCouponQuantityIssued();
		//null 인경우 무제한 발급
		if (maxCoupon != null){
			if (maxCoupon == 0 || issuedCouponCount >= maxCoupon){
				throw new IllegalArgumentException("발급 가능한 수량이 모두 소진되었습니다.");
			}
		}

		//받았는지 확인
		if (userCouponRepository.existsByUser_IdAndCoupon_Id(userId,couponId)){
			throw new IllegalArgumentException("이미 발급받은 쿠폰입니다.");
		}

		UserCoupon userCoupon = new UserCoupon(user,coupon);
		UserCoupon saved = userCouponRepository.save(userCoupon);

		return new UserCouponResponseDto(
			saved.getId(),
			coupon.getName(),
			coupon.getDiscountType(),
			saved.isUsed(),
			saved.getIssuedAt()
		);
	}
}
