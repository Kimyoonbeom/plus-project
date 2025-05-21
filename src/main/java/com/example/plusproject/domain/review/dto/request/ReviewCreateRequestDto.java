package com.example.plusproject.domain.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {

	private final String content;

	private final Double rating;

	public ReviewCreateRequestDto(String content, Double rating) {
		this.content = content;
		this.rating = rating;
	}
}
