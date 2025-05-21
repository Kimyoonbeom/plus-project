package com.example.plusproject.domain.search.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.plusproject.domain.search.dto.response.KeywordCount;
import com.example.plusproject.domain.search.entity.SearchLog;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
	//JPQL 방식 일반 DB에서 인기검색어 추출
	@Query("SELECT s.keyword FROM SearchLog s GROUP BY s.keyword ORDER BY COUNT(s.keyword) DESC LIMIT 10")
	List<String> findTop10Keywords();

	// JPQL은 **2개 컬럼(keyword, count)**을 조회하는데, List<String>은 단일 컬럼만 매핑 가능하기 때문에 List<Objectp[]> 로 불러와야 한다고 함.
	// 또는 키워드와 카운트 반환용 DTO로 가능, 이게 나을 것 같네...
	// @Query("SELECT s.keyword AS keyword, COUNT(s) AS cnt FROM SearchLog s GROUP BY s.keyword ORDER BY cnt DESC")
	// List<String> findTopKeywords(Pageable pageable);

	@Query("SELECT new com.example.plusproject.domain.search.dto.response.KeywordCount(s.keyword, COUNT(s)) " +
	"FROM SearchLog s GROUP BY s.keyword ORDER BY COUNT(s) DESC")
	List<KeywordCount> findTopKeywords(Pageable pageable);
}
