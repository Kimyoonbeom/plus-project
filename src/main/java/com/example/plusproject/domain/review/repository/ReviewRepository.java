package com.example.plusproject.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.plusproject.domain.review.dto.response.ReviewSearchResponseDto;
import com.example.plusproject.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	default Review findByIdOrElseThrow(Long reviewId) {
		return findById(reviewId).orElseThrow(() -> new RuntimeException("찾는 리뷰가 없습니다"));
	}

	List<Review> findAllByBook_IdAndStatus(Long bookId, boolean status);

	Page<ReviewSearchResponseDto> findAllByContentContainingAndStatus(String content, boolean status);

	String content(String content);

	// @Query("SELECT new com.example.plusproject.domain.review.dto.response.ReviewSearchResponseDto(r.content, rl.likeReview) "
	// 	+ "FROM ReviewLike rl "
	// 	+ "JOIN rl.review r"
	// 	+ "ORDER BY rl.likeReview DESC")
	// Page<ReviewSearchResponseDto> findAllOrderByLikeDesc(Pageable pageable);

	Page<Review> findAllByContentContainingAndStatus(String content, boolean status, Pageable pageable);

}
