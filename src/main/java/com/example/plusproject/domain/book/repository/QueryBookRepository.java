package com.example.plusproject.domain.book.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.plusproject.domain.book.dto.AladinItemResponse;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.dto.NaverBookResponse;
import com.example.plusproject.domain.book.entity.Book;

public interface QueryBookRepository {
	Page<BookResponseDto> findByTitleAndAuthor(String keyword, Pageable pageable);
	List<Book> findExistingAladinBooks(List<AladinItemResponse.AladinItem> items);
	List<Book> findExistingNaverBooks(List<NaverBookResponse.NaverItem> items);
}
