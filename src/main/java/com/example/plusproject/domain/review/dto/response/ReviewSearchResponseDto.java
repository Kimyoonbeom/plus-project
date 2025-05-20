package com.example.plusproject.domain.review.dto.response;

import lombok.Getter;

@Getter
public class ReviewSearchResponseDto {

	private final String content;

	private final int likeReview;

	public ReviewSearchResponseDto(String content, int likeReview) {
		this.content = content;
		this.likeReview = likeReview;
	}
}
