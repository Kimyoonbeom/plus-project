package com.example.plusproject.domain.review.dto.response;

import com.example.plusproject.domain.review.entity.Review;

import lombok.Getter;

@Getter
public class ReviewDeleteResponseDto {

	private final Long bookId;

	private final Long id;

	private final String content;

	private final Boolean status;

	private ReviewDeleteResponseDto(Review review) {
		this.bookId = review.getBook().getId();
		this.id = review.getId();
		this.content = review.getContent();
		this.status = review.isStatus();
	}

	public static ReviewDeleteResponseDto fromReview(Review review) {
		return new ReviewDeleteResponseDto(review);
	}
}
