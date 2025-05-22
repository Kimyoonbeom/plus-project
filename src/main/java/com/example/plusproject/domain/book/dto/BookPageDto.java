package com.example.plusproject.domain.book.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookPageDto {
	private List<BookResponseDto> content;
	private int page;
	private int size;
	private Long totalCount;

	public BookPageDto(List<BookResponseDto> content, int page, int size, Long totalCount) {
		this.content = content;
		this.page = page;
		this.size = size;
		this.totalCount = totalCount;
	}

	public Page<BookResponseDto> toPage(){
		return new PageImpl<>(content, PageRequest.of(page,size), totalCount);
	}
}
