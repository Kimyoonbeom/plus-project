package com.example.plusproject.domain.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.review.dto.request.ReviewCreateRequestDto;
import com.example.plusproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.plusproject.domain.review.dto.response.ReviewCreateResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewDeleteResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.plusproject.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/{bookId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 생성
	 * @param bookId
	 * @param dto
	 * @param authUser
	 * @return ResponseEntity<ReviewCreateResponseDto> 응답 형태
	 */
	@PostMapping
	public ResponseEntity<ReviewCreateResponseDto> saveReview(
		@PathVariable Long bookId,
		@RequestBody ReviewCreateRequestDto dto,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return new ResponseEntity<>(reviewService.saveReview(bookId, dto, authUser), HttpStatus.CREATED);
	}

	/**
	 * 리뷰 조회
	 * @param bookId
	 * @return ResponseEntity<List<ReviewResponseDto>> 응답 형태
	 */
	@GetMapping
	public ResponseEntity<List<ReviewResponseDto>> findReview(
		@PathVariable Long bookId
	) {
		return new ResponseEntity<>(reviewService.findAll(bookId), HttpStatus.OK);
	}

	/**
	 * 리뷰 수정
	 * @param bookId
	 * @param reviewId
	 * @param dto
	 * @param authUser
	 * @return ResponseEntity<ReviewUpdateResponseDto> 응답 형태
	 */
	@PatchMapping("/{reviewId}")
	public ResponseEntity<ReviewUpdateResponseDto> updateReview(
		@PathVariable Long bookId,
		@PathVariable Long reviewId,
		@RequestBody ReviewUpdateRequestDto dto,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return new ResponseEntity<>(reviewService.updateReview(bookId, reviewId, dto, authUser), HttpStatus.OK);
	}

	/**
	 * 리뷰 삭제
	 * soft delete 방식
	 * @param bookId
	 * @param reviewId
	 * @param authUser
	 * @return ResponseEntity<ReviewDeleteResponseDto> 응답 형태
	 */
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<ReviewDeleteResponseDto> deleteReview(
		@PathVariable Long bookId,
		@PathVariable Long reviewId,
		@AuthenticationPrincipal AuthUser authUser
	) {
		return new ResponseEntity<>(reviewService.deleteReview(bookId, reviewId, authUser), HttpStatus.OK);
	}
}
