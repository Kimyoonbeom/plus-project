package com.example.plusproject.domain.book.service;

import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class BookIndexTest {

	@Autowired
	private BookRepository bookRepository;

	// private final Random random = new Random();
	//
	// @Test
	// @Order(1)
	// @DisplayName("책 저장")
	// void generateMockBooks() {
	// 	List<Book> books = new ArrayList<>();
	//
	// 	for (int i = 1; i <= 1000; i++) {
	// 		BookResponseDto dto = new BookResponseDto(
	// 			null,
	// 			"토끼의 모험 " + i,
	// 			"작가 " + (i % 50),
	// 			"출판사 " + (i % 20),
	// 			"이 책은 토끼가 주인공인 모험 이야기입니다. 책 번호: " + i,
	// 			10000 + random.nextInt(5000),
	// 			5 + random.nextInt(10),
	// 			"https://example.com/image" + i + ".jpg",
	// 			random.nextDouble() * 5
	// 		);
	//
	// 		Book book = Book.builder()
	// 			.title(dto.getTitle())
	// 			.author(dto.getAuthor())
	// 			.publisher(dto.getPublisher())
	// 			.description(dto.getDescription())
	// 			.price(dto.getPrice())
	// 			.stock(dto.getStock())
	// 			.imageUrl(dto.getImageUrl())
	// 			.rating(dto.getRating())
	// 			.publishedAt(LocalDate.of(2023, random.nextInt(12) + 1, random.nextInt(28) + 1))
	// 			.build();
	//
	// 		books.add(book);
	// 	}
	//
	// 	bookRepository.saveAll(books);
	// 	System.out.println("✅ 책 저장 완료: 총 " + books.size() + "권");
	// }

	@Test
	@Order(2)
	@DisplayName("인덱스 ON 상태에서 검색 성능 테스트")
	void testSearchWithIndex() {
		long start = System.currentTimeMillis();

		List<Book> results = bookRepository.findByTitleContaining("토끼");

		long end = System.currentTimeMillis();
		System.out.println("🔍 인덱스 ON 상태 검색 시간: " + (end - start) + "ms");
		System.out.println("검색 결과 수: " + results.size());
	}

	@Test
	@Order(3)
	@DisplayName("인덱스 OFF 상태에서 검색 성능 테스트")
	void testSearchWithoutIndex() {
		long start = System.currentTimeMillis();

		List<Book> results = bookRepository.findByTitleContaining("토끼");

		long end = System.currentTimeMillis();
		System.out.println("🔍 인덱스 OFF 상태 검색 시간: " + (end - start) + "ms");
		System.out.println("검색 결과 수: " + results.size());
	}
}
