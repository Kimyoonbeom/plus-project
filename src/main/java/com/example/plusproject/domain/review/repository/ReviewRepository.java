package com.example.plusproject.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.plusproject.domain.review.dto.response.ReviewSearchResponseDto;
import com.example.plusproject.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	default Review findByIdOrElseThrow(Long reviewId) {
		return findById(reviewId).orElseThrow(() -> new RuntimeException("찾는 리뷰가 없습니다"));
	}

	List<Review> findAllByBook_IdAndStatus(Long bookId, boolean status);

	// @Query("SELECT new com.example.plusproject.domain.review.dto.response.ReviewSearchResponseDto(r.content, r.likeReview) "
	// 	+ "FROM Review r "
	// 	+ "ORDER BY r.likeReview DESC")
	// Page<ReviewSearchResponseDto> findAllOrderByLikeDesc(Pageable pageable);
}
