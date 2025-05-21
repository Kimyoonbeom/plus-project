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
	private final QueryBookRepository queryBookRepository;
	private final LocalCacheRank localCacheRank;

	//검색
	public Page<BookResponseDto> search1(String userId, String keyword, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookResponseDto> list = queryBookRepository.findByTitleAndAuthor(keyword,pageable);

		return list;
	}

	//인기 검색어, 디비 검색
	public List<String> getTopKeywordsWithDB(){
		System.out.println("\uD83E\uDD55\uD83E\uDD55 검색(디비) 메서드 진입 \uD83E\uDD55\uD83E\uDD55");
		List<KeywordCount> list = searchLogRepository.findTopKeywords(PageRequest.of(0,10));
		return list.stream().map(KeywordCount::getKeyword).toList();
	}

	//인기 검색어, 로컬 캐시 검색
	@Cacheable(value = "bookSearchLocal", key = "'Top10'", cacheManager = "localCacheManager")
	public List<String> getTopKeywordsWithLocalCache(){
		System.out.println("\uD83E\uDD55\uD83E\uDD55 검색(로컬 캐시) 메서드 진입 \uD83E\uDD55\uD83E\uDD55");
		List<String> result = localCacheRank.getTopKeywords();
		if(result == null|| result.isEmpty()){
			return getTopKeywordsWithDB();
		}

		return result;
	}

	//인기 검색어, 레디스 캐시 검색
	@Cacheable(value = "bookSearchRedis", key = "'Top10'", cacheManager = "redisCacheManager")
	public List<String> getTopKeywordsWithRedis() {
		System.out.println("\uD83E\uDD55\uD83E\uDD55 검색(레디스) 메서드 진입 \uD83E\uDD55\uD83E\uDD55");
		Set<ZSetOperations.TypedTuple<String>> result =
			redisTemplate.opsForZSet().reverseRangeWithScores("popular_keywords",0,9);

		if(result == null || result.isEmpty()){
			return getTopKeywordsWithDB();
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
