package com.example.plusproject.domain.search.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	private final RedisTemplate<String, String> redisTemplate;
	private final BookService bookService;
	private final QueryBookRepository queryBookRepository;
	private final LocalCacheRank localCacheRank;

	// 캐시 미사용 검색
	public Page<BookResponseDto> search1(String userId, String keyword, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page - 1, size);

		// keyword = keyword.trim().toLowerCase();

		Page<BookResponseDto> list = queryBookRepository.findByTitleAndAuthor(keyword,pageable);
		// searchLogRepository.save(new SearchLog(userId, keyword, LocalDateTime.now()));

		return list;
	}

	// 로컬 인메모리 사용
	@Cacheable(value = "bookSearchLocal", key = "#keyword", cacheManager = "localCacheManager")
	public Page<BookResponseDto> search2(String userId, String keyword, Integer page, Integer size) {
		System.out.println("🧠 캐시 미스 → 실제 검색 로직 수행: " + keyword); // 첫 요청에만 출력되어야 함
		Pageable pageable = PageRequest.of(page - 1, size);
		if (keyword.length() < 2 || keyword.matches(".*[!@#$%^&*ㄱ-ㅎㅏ-ㅣ].*")) {
			throw new IllegalArgumentException("검색어는 2자 이상이어야 하며 특수문자나 자음/모음만으로 구성될 수 없습니다.");
		}

		// keyword = keyword.trim().toLowerCase();
		// searchLogRepository.save(new SearchLog(userId, keyword, LocalDateTime.now()));

		localCacheRank.addKeyword(keyword);

		return queryBookRepository.findByTitleAndAuthor(keyword,pageable);
	}

	// 레디스(리모트) 인메모리 사용
	@Cacheable(value = "bookSearchRedis", key = "#keyword", cacheManager = "redisCacheManager")
	public BookPageDto search3(String userId, String keyword, Integer page, Integer size) {
		System.out.println("🧠 레디스 검색 메서드 호출 진입 🧠"); // 첫 요청에만 출력되어야 함

		Pageable pageable = PageRequest.of(page - 1, size);
		if (keyword.length() < 2 || keyword.matches(".*[!@#$%^&*ㄱ-ㅎㅏ-ㅣ].*")) {
			throw new IllegalArgumentException("검색어는 2자 이상이어야 하며 특수문자나 자음/모음만으로 구성될 수 없습니다.");
		}

		// keyword = keyword.trim().toLowerCase();
		// searchLogRepository.save(new SearchLog(userId, keyword, LocalDateTime.now()));
		redisTemplate.opsForZSet().incrementScore("popular_keywords",keyword,1);

		Page<BookResponseDto> pageResult = queryBookRepository.findByTitleAndAuthor(keyword,pageable);

		return new BookPageDto(pageResult.getContent(),page,size,pageResult.getTotalElements());
	}

	public List<String> getTopKeywordsWithDB(){
		List<KeywordCount> list = searchLogRepository.findTopKeywords(PageRequest.of(0,10));
		return list.stream().map(KeywordCount::getKeyword).toList();
	}

	public List<String> getTopKeywordsWithLocalCache(){
		return localCacheRank.getTopKeywords();
	}

	public List<String> getTopKeywordsWithRedis() {
		Set<ZSetOperations.TypedTuple<String>> result =
			redisTemplate.opsForZSet().reverseRangeWithScores("popular_keywords",0,9);

		if(result == null){
			throw new RuntimeException("검색어가 없습니다.");
		}

		List<String> rankedList = new ArrayList<>();
		int rank = 1;

		for(ZSetOperations.TypedTuple<String> tuple : result){
			String formatted = rank++ + ". " + tuple.getValue();
			rankedList.add(formatted);
		}
		return rankedList;
	}

	public void saveSearchLog(String userId, String keyword){
		SearchLog log = new SearchLog(userId, keyword, LocalDateTime.now());
		searchLogRepository.save(log);
	}


}
