package com.example.plusproject.domain.review.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plusproject.domain.review.dto.response.ReviewSearchResponseDto;
import com.example.plusproject.domain.review.service.ReviewSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews/search")
@RequiredArgsConstructor
public class ReviewSearchController {

	private final ReviewSearchService reviewSearchService;

	// @GetMapping
	// public ResponseEntity<Page<ReviewSearchResponseDto>> searchReview(
	// 	@PageableDefault(size = 3) Pageable pageable
	// ) {
	//
	// 	return new ResponseEntity<>(reviewSearchService.searchReview(pageable), HttpStatus.OK);
	// }
}
