package com.example.plusproject.domain.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plusproject.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	default Review findByIdOrElseThrow(Long reviewId) {
		return findById(reviewId).orElseThrow(() -> new RuntimeException("찾는 리뷰가 없습니다"));
	}

	List<Review> findAllByBook_IdAndStatus(Long bookId, boolean status);

}
