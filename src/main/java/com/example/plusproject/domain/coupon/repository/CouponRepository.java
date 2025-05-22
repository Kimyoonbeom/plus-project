package com.example.plusproject.domain.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.example.plusproject.domain.coupon.entity.Coupon;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

	/**
	 * 비관적 락
	 * 적용 방식 : PESSIMISTIC_WRITE(락 획득 못했을 경우 접근 불가)
	 * 데드락 방지를 위해 타임아웃 적용 (1초)
	 * @param couponId
	 * @return
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")})
	@Query("SELECT c FROM Coupon c WHERE c.id = :couponId ORDER BY c.id")
	Optional<Coupon> findByIdWithPessimisticLock(@Param("couponId") Long couponId);

	default Coupon findByIdWithPessimisticLockOrElseThrow(Long couponId) {
		return findByIdWithPessimisticLock(couponId).orElseThrow(() -> new RuntimeException("존재하지 않는 쿠폰입니다"));
	}
}
