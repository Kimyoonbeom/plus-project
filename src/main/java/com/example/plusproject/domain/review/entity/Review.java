package com.example.plusproject.domain.review.entity;

import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.plusproject.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;

	private String content;

	private double rating;

	private boolean status = false;

	public Review(User user, Book book, String content, double rating) {
		this.user = user;
		this.book = book;
		this.content = content;
		this.rating = rating;
	}

	/**
	 * soft delete 메서드
	 * 기본 status = false
	 * 삭제 시 status = true
	 */
	public void deleteReview () {
		this.status = true;
	}

	/**
	 * 리뷰 수정을 위한 메서드
	 * @param dto
	 */
	public void update(ReviewUpdateRequestDto dto) {
		this.content = dto.getContent();
		this.rating = dto.getRating();
	}
}
