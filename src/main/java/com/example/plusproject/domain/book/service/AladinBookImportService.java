package com.example.plusproject.domain.book.service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.plusproject.domain.book.dto.AladinItemResponse;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.book.repository.QueryBookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AladinBookImportService {

	private final BookRepository bookRepository;
	private final QueryBookRepository queryBookRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${aladin.api.key}")
	private String ttbKey;

	public void importBooksFromAladin(List<String> queries) {
		for (String query : queries) {
			int startPage = 1;
			int totalInsert = 0;
			int duplicatePagecount = 0;

			while (totalInsert < 200) {
				String url = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
					"?ttbkey=" + ttbKey +
					"&Query=" + query +
					"&Start=" + startPage +
					"&MaxResults=50" +
					"&SearchTarget=Book" +
					"&output=js" +
					"&Version=20131101";

				ResponseEntity<AladinItemResponse> response =
					restTemplate.getForEntity(url, AladinItemResponse.class);

				if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
					List<AladinItemResponse.AladinItem> items = response.getBody().getItem();

					if (items == null || items.isEmpty())
						break;

					List<Book> existingBooks = queryBookRepository.findExistingAladinBooks(items);
					//중복 확인용 키 생성, title, author가 같으면 중복이니까 → 이 2개를 이어붙여서 하나의 식별자(key) 처럼 씀.
					//예: "채식주의자|한강" ← 이게 key
					Set<String> existingKeys = existingBooks.stream()
						.map(b -> toKey(b.getTitle(), b.getAuthor()))
						.collect(Collectors.toSet());

					List<Book> booksToSave = items.stream()
						.filter(item -> {
							String key = toKey(item.getTitle(), item.getAuthor());
							boolean isDuplicate = existingKeys.contains(key);
							return !isDuplicate;
						}).map(this::toEntity).toList();

					if (booksToSave.isEmpty()) {
						duplicatePagecount++;
						if (duplicatePagecount >= 3) {
							break;
						}
					} else {
						try{
							duplicatePagecount = 0;
							bookRepository.saveAll(booksToSave);
							totalInsert += booksToSave.size();
						}catch (DataIntegrityViolationException e){
							System.out.println("⚠️ 중복 도서 저장 시도 발생: " + e.getMessage());
						}
					}

					System.out.println("🧩 키워드: " + query);
					System.out.println("🧩 수신된 item 수: " + items.size());
					System.out.println("🧩 저장 예정 도서 수: " + booksToSave.size());
					System.out.println("🧩 누적 저장 수: " + totalInsert);
					System.out.println("🧩 현재 페이지: " + startPage);
					if (items.size() < 50 || totalInsert >= 200)
						break;

					startPage += 1;
				} else {
					break;
				}
			}
		}
	}

	public Book toEntity(AladinItemResponse.AladinItem item) {
		return Book.builder()
			.title(normalize(item.getTitle()))
			.author(normalize(item.getAuthor()))
			.description(item.getDescription())
			.publisher(normalize(item.getPublisher()))
			.publishedAt(item.getPublishedAt())
			.stock(5)
			.price(item.getPrice())
			.rating(0.0)
			.imageUrl(item.getImageUrl())
			.build();
	}

	private String toKey(String title, String author) {
		return normalize(title) + "|" + normalize(author);
	}

	private String normalize(String input){
		if(input == null) return "";
		input = input.trim();
		return input;
	}

}
