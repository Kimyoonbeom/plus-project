package com.example.plusproject.domain.search.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class LocalCacheRank {
	private final ConcurrentHashMap<String, AtomicInteger> keywordMap = new ConcurrentHashMap<>();

	public void addKeyword(String keyword){
		keywordMap.computeIfAbsent(keyword, k-> new AtomicInteger(0)).incrementAndGet();
	}

	public List<String> getTopKeywords(){
		return keywordMap.entrySet().stream()
			.sorted((a, b)->Integer.compare(b.getValue().get(), a.getValue().get()))
			.limit(10)
			.map(Map.Entry::getKey)
			.toList();
	}
}
