package com.example.plusproject.domain.review.dto.response;

import java.time.LocalDateTime;

import com.example.plusproject.domain.review.entity.Review;

import lombok.Getter;

@Getter
public class ReviewResponseDto {

	private final Long bookId;

	private final Long id;

	private final String content;

	private final Double rating;

	private final LocalDateTime updatedAt;

	private ReviewResponseDto(Review review) {
		this.bookId = review.getBook().getId();
		this.id = review.getId();
		this.content = review.getContent();
		this.rating = review.getRating();
		this.updatedAt = review.getUpdatedAt();
	}

	public static ReviewResponseDto fromReview(Review review) {
		return new ReviewResponseDto(review);
	}
}
