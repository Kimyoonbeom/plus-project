package com.example.plusproject.domain.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.plusproject.domain.book.dto.AladinItemResponse;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AladinBookImportService {

	private final BookRepository bookRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${aladin.api.key}")
	private String ttbKey;

	public void importBooksFromAladin(List<String> queries) {
		for (String query : queries) {
			int startPage = 1;
			int totalInsert = 0;

			while (totalInsert < 3000) {
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

					List<Book> books = items.stream().map(this::toEntity).toList();
					bookRepository.saveAll(books);

					totalInsert += books.size();
					startPage += 1;
				} else {
					break;
				}
			}
		}
	}

	public Book toEntity(AladinItemResponse.AladinItem item) {
		return Book.builder()
			.title(item.getTitle())
			.author(item.getAuthor())
			.description(item.getDescription())
			.publisher(item.getPublisher())
			.publishedAt(item.getPublishedAt())
			.stock(5)
			.price(item.getPrice())
			.writerName("USER1")
			.password("test12!")
			.rating(0.0)
			.build();
	}
}
