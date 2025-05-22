package com.example.plusproject.domain.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.plusproject.domain.book.dto.BookResponseDto;

public interface QueryBookRepository {
	Page<BookResponseDto> findByTitleAndAuthor(String keyword, Pageable pageable);
}
