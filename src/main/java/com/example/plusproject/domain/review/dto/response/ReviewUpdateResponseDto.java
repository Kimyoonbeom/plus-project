package com.example.plusproject.domain.review.dto.response;

import java.time.LocalDateTime;

import com.example.plusproject.domain.review.entity.Review;

import lombok.Getter;

@Getter
public class ReviewUpdateResponseDto {

	private final Long bookId;

	private final Long id;

	private final String content;

	private final LocalDateTime updatedAt;

	private ReviewUpdateResponseDto(Review review) {
		this.bookId = review.getBook().getId();
		this.id = review.getId();
		this.content = review.getContent();
		this.updatedAt = review.getUpdatedAt();
	}

	public static ReviewUpdateResponseDto fromReview(Review review) {
		return new ReviewUpdateResponseDto(review);
	}
}
