package com.example.plusproject.domain.review.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.plusproject.domain.review.dto.response.ReviewSearchResponseDto;
import com.example.plusproject.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewSearchService {

	private final ReviewRepository reviewRepository;

	/**
	 * JPQL 사용
	 * JPQL은 LIMIT 사용이 불가능 하므로 Pageable 사용해야한다
	 * @return
	 */
	// public Page<ReviewSearchResponseDto> searchReview(Pageable pageable) {
	//
	// 	return reviewRepository.findAllOrderByLikeDesc(PageRequest.of(0, 3));
	// }


}
