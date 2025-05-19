package com.example.plusproject.domain.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {

	private final String content;

	private final Double rating;

	public ReviewUpdateRequestDto(String content, Double rating) {
		this.content = content;
		this.rating = rating;
	}
}
