package com.example.plusproject.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.review.dto.request.ReviewCreateRequestDto;
import com.example.plusproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.plusproject.domain.review.dto.response.ReviewCreateResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewDeleteResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewResponseDto;
import com.example.plusproject.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.plusproject.domain.review.entity.Review;
import com.example.plusproject.domain.review.repository.ReviewRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	/**
	 * 리뷰 생성
	 * @param bookId
	 * @param dto
	 * @param authUser
	 * @return ReviewCreateResponseDto 응답 형태
	 */
	@Transactional
	@Override
	public ReviewCreateResponseDto saveReview(Long bookId, ReviewCreateRequestDto dto, AuthUser authUser) {

		Book findBook = bookRepository.findByIdOrElseThrow(bookId);

		User findUser =  userRepository.findById(authUser.getId()).orElseThrow(() -> new RuntimeException("찾는 유저가 없습니다"));

		Review review = new Review(findUser, findBook, dto.getContent(), dto.getRating());

		reviewRepository.save(review);

		return ReviewCreateResponseDto.fromReview(review);
	}

	/**
	 * 리뷰 조회
	 * @param bookId
	 * @return List<ReviewResponseDto> 응답 형태
	 */
	@Override
	public List<ReviewResponseDto> findAll(Long bookId) {

		List<Review> findAllReviewByBookId = reviewRepository.findAllByBook_IdAndStatus(bookId, false);

		return findAllReviewByBookId.stream().map(ReviewResponseDto::fromReview).toList();
	}

	/**
	 * 리뷰 수정
	 * @param bookId
	 * @param reviewId
	 * @param dto
	 * @param authUser
	 * @return ReviewUpdateResponseDto 응답 형태
	 */
	@Transactional
	@Override
	public ReviewUpdateResponseDto updateReview(Long bookId, Long reviewId, ReviewUpdateRequestDto dto, AuthUser authUser) {

		bookRepository.findByIdOrElseThrow(bookId);

		Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

		findReview.update(dto);

		return ReviewUpdateResponseDto.fromReview(findReview);
	}

	/**
	 * 리뷰 삭제
	 * soft delete 방식
	 * @param bookId
	 * @param reviewId
	 * @param authUser
	 * @return ReviewDeleteResponseDto 응답 형태
	 */
	@Transactional
	@Override
	public ReviewDeleteResponseDto deleteReview(Long bookId, Long reviewId, AuthUser authUser) {

		bookRepository.findByIdOrElseThrow(bookId);

		Review findReview = reviewRepository.findByIdOrElseThrow(reviewId);

		findReview.deleteReview();

		return ReviewDeleteResponseDto.fromReview(findReview);
	}
}
