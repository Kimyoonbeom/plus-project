package com.example.plusproject.domain.review.service;

import java.util.List;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.review.dto.request.ReviewCreateRequestDto;
import com.example.plusproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.plusproject.domain.review.dto.response.ReviewCreateResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewDeleteResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewUpdateResponseDto;

public interface ReviewService {
	ReviewCreateResponseDto saveReview(Long bookId, ReviewCreateRequestDto dto, AuthUser authUser);

	List<ReviewResponseDto> findAll(Long bookId);

	ReviewUpdateResponseDto updateReview(Long bookId, Long reviewId, ReviewUpdateRequestDto dto, AuthUser authUser);

	ReviewDeleteResponseDto deleteReview(Long bookId, Long reviewId, AuthUser authUser);
}
