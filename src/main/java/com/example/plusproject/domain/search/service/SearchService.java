package com.example.plusproject.domain.search.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.plusproject.domain.book.dto.BookPageDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.repository.QueryBookRepository;
import com.example.plusproject.domain.book.service.BookService;
import com.example.plusproject.domain.search.dto.response.KeywordCount;
import com.example.plusproject.domain.search.entity.SearchLog;
import com.example.plusproject.domain.search.repository.SearchLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchLogRepository searchLogRepository;
	private final QueryBookRepository queryBookRepository;

	//검색
	public Page<BookResponseDto> search1(String keyword, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page - 1, size);

		return queryBookRepository.findByTitleAndAuthor(keyword,pageable);
	}

	// 로컬 인메모리 사용
	@Cacheable(value = "bookSearchLocal", key = "'SearchCache'", cacheManager = "localCacheManager")
	public Page<BookResponseDto> search2(String keyword, Integer page, Integer size) {
		System.out.println("🧠🧠 로컬 캐시 메서드 진입 🧠🧠"); // 첫 요청에만 출력되어야 함
		Pageable pageable = PageRequest.of(page - 1, size);
		if (keyword.length() < 2 || keyword.matches(".*[!@#$%^&*ㄱ-ㅎㅏ-ㅣ].*")) {
			throw new IllegalArgumentException("검색어는 2자 이상이어야 하며 특수문자나 자음/모음만으로 구성될 수 없습니다.");
		}

		return queryBookRepository.findByTitleAndAuthor(keyword,pageable);
	}

	// 레디스(리모트) 인메모리 사용
	@Cacheable(value = "bookSearchRedis", key = "'SearchCache'", cacheManager = "redisCacheManager")
	public BookPageDto search3(String keyword, Integer page, Integer size) {
		System.out.println("🧠 레디스 검색 메서드 호출 진입 🧠"); // 첫 요청에만 출력되어야 함

		Pageable pageable = PageRequest.of(page - 1, size);
		if (keyword.length() < 2 || keyword.matches(".*[!@#$%^&*ㄱ-ㅎㅏ-ㅣ].*")) {
			throw new IllegalArgumentException("검색어는 2자 이상이어야 하며 특수문자나 자음/모음만으로 구성될 수 없습니다.");
		}

		Page<BookResponseDto> pageResult = queryBookRepository.findByTitleAndAuthor(keyword,pageable);

		return new BookPageDto(pageResult.getContent(),page,size,pageResult.getTotalElements());
	}

	//인기 검색어, 디비 검색
	public List<String> getTopKeywordsWithDB(){
		System.out.println("\uD83E\uDD55\uD83E\uDD55 인기 검색어(디비) 메서드 진입 \uD83E\uDD55\uD83E\uDD55");
		List<KeywordCount> list = searchLogRepository.findTopKeywords(PageRequest.of(0,10));
		// return list.stream().map(KeywordCount::getKeyword).toList();
		return new ArrayList<>(
			list.stream()
				.map(KeywordCount::getKeyword)
				.collect(Collectors.toList())
		);
	}

	//인기 검색어, 로컬 캐시 검색
	@Cacheable(value = "TopKeywordLocal", key = "'Top10'", cacheManager = "localCacheManager")
	public List<String> getTopKeywordsWithLocalCache(){
		System.out.println("\uD83E\uDD55\uD83E\uDD55 인기 검색어(로컬 캐시) 메서드 진입 \uD83E\uDD55\uD83E\uDD55");
		return getTopKeywordsWithDB();
	}

	//인기 검색어, 레디스 캐시 검색
	@Cacheable(value = "TopKeywordRedis", key = "'Top10'", cacheManager = "redisCacheManager")
	public List<String> getTopKeywordsWithRedis() {
		System.out.println("\uD83E\uDD55\uD83E\uDD55 인기 검색어(레디스) 메서드 진입 \uD83E\uDD55\uD83E\uDD55");
		return getTopKeywordsWithDB();
	}

	public void saveSearchLog(String userId, String keyword){
		SearchLog log = new SearchLog(userId, keyword, LocalDateTime.now());
		searchLogRepository.save(log);
	}


}
