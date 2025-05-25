package com.example.plusproject.domain.book.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import com.example.plusproject.domain.book.dto.NaverBookResponse;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.book.repository.QueryBookRepository;

@Service
@RequiredArgsConstructor
public class NaverBookSearchService {

	private final RestTemplateBuilder restTemplateBuilder;
	private final BookRepository bookRepository;
	private final QueryBookRepository queryBookRepository;

	@Value("${naver.api.client-id}")
	private String clientId;

	@Value("${naver.api.client-secret}")
	private String clientSecret;

	private static final String API_URL = "https://openapi.naver.com/v1/search/book.json";

	public void importBooksFromNaver(List<String> queries) {
		RestTemplate restTemplate = restTemplateBuilder.build();

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		System.out.println("📌 Client ID: " + clientId);
		requestEntity.getHeaders().forEach((k, v) -> System.out.println(k + ": " + v));

		for (String query : queries) {
			System.out.println("🥕🥕🥕 검색어 : " + query +" 🥕🥕🥕");
			String url = API_URL + "?query=" + query + "&display=1&start=1";
			try {
				ResponseEntity<NaverBookResponse> response = restTemplate.exchange(
					url, HttpMethod.GET, requestEntity, NaverBookResponse.class);
				if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
					int total = response.getBody().getTotal();
					int limit = Math.min(total, 1000);
					System.out.println("🔍 키워드: " + query + " | 총 결과 수: " + total);

					List<NaverBookResponse.NaverItem> totalItems = new ArrayList<>();

					for (int start = 1; start <= limit; start += 100) {
						String pagedUrl = API_URL + "?query=" + query + "&display=100&start=" + start + "&sort=sim";
						ResponseEntity<NaverBookResponse> secondResponse = restTemplate.exchange(
							pagedUrl, HttpMethod.GET, requestEntity, NaverBookResponse.class);

						if (secondResponse.getStatusCode().is2xxSuccessful() && secondResponse.getBody() != null) {
							List<NaverBookResponse.NaverItem> items = secondResponse.getBody().getItem();

							if (items == null || items.isEmpty())
								break;

							totalItems.addAll(items);
						}
					}
					Map<String, NaverBookResponse.NaverItem> uniqueItems = totalItems.stream()
						.collect(Collectors.toMap(
							item -> normalizedKey(item.getTitle(), item.getAuthor()),
							item -> item,
							(existing, replacement) -> existing // 중복 시 첫 번째만 사용
						));

					Set<String> existingKeys = queryBookRepository.findExistingNaverBooks(
							new ArrayList<>(uniqueItems.values())
						).stream()
						.map(b -> normalizedKey(b.getTitle(), b.getAuthor()))
						.collect(Collectors.toSet());

					List<Book> booksToSave = uniqueItems.entrySet().stream()
						.filter(entry -> !existingKeys.contains(entry.getKey()))
						.map(entry -> toEntity(entry.getValue()))
						.toList();

					System.out.println("⭐ totalItems 수집 개수: " + totalItems.size());
					System.out.println("⭐ 기존 도서 개수: " + existingKeys.size());
					System.out.println("⭐ 저장 대상 도서 개수: " + booksToSave.size());
					try {
						bookRepository.saveAll(booksToSave);
					} catch (DataIntegrityViolationException e) {
						System.err.println("⚠️ 중복 도서 저장 실패: " + e.getMessage());
					}
				}
			} catch (Exception e) {
				System.err.println("❌ 전체 요청 실패: " + e.getMessage());
			}
		}

	}

	public Book toEntity(NaverBookResponse.NaverItem item) {
		return Book.builder()
			.title(cleanHtml(item.getTitle()))
			.author(cleanHtml(item.getAuthor()))
			.description(cleanHtml(item.getDescription()))
			.publisher(item.getPublisher())
			.publishedAt(parseDate(item.getPubdate()))
			.stock(5)
			.price(item.getDiscount())
			.imageUrl(item.getImage())
			.rating(0.0)
			.build();
	}

	private String cleanHtml(String str) {
		if (str == null)
			return "";
		return str.replaceAll("<[^>]+>", "") // 태그 제거
			.replaceAll("&#?[a-zA-Z0-9]+;", "")
			.replaceAll("\\s+", " ") // 공백 정리
			.trim();
	}

	private LocalDate parseDate(String pubdate) {
		if (pubdate == null || pubdate.isBlank()) return null;
		try {
			return LocalDate.parse(pubdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
		} catch (Exception e) {
			System.err.println("⚠️ 날짜 파싱 실패: " + pubdate);
			return null;
		}
	}

	private String normalizedKey(String title, String author) {
		return cleanHtml(title).replaceAll("\\s+", "").replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase()
			+ "|" +
			cleanHtml(author).replaceAll("\\s+", "").replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase();
	}

}
