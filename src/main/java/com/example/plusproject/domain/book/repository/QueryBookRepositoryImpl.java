package com.example.plusproject.domain.book.repository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.plusproject.domain.book.dto.AladinItemResponse;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.dto.NaverBookResponse;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryBookRepositoryImpl implements QueryBookRepository {
	private final EntityManager entityManager;

	@Override
	public Page<BookResponseDto> findByTitleAndAuthor(String keyword, Pageable pageable) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QBook book = QBook.book;

		BooleanBuilder builder = new BooleanBuilder();
		BooleanBuilder orBuilder = new BooleanBuilder();
		if (keyword != null) {
			orBuilder.or(book.title.contains(keyword));
			orBuilder.or(book.author.contains(keyword));
		}

		builder.and(orBuilder);

		List<BookResponseDto> list = queryFactory
			.select(Projections.constructor(BookResponseDto.class,
				book.id,
				book.title,
				book.author,
				book.publisher,
				book.description,
				book.price,
				book.stock,
				book.imageUrl,
				book.rating))
			.from(book)
			.where(builder)
			.orderBy(book.publishedAt.desc().nullsLast(), book.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long countResult = queryFactory
			.select(book.count())
			.from(book)
			.where(builder).fetchOne();

		Long total = (countResult != null) ? countResult : 0;

		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public List<Book> findExistingNaverBooks(List<NaverBookResponse.NaverItem> items) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QBook book = QBook.book;

		// 넉넉하게 필터링: 제목 또는 저자 유사한 거 다 긁어오기 (최종 필터는 자바에서!)
		/*BooleanBuilder outer = new BooleanBuilder();
		for (NaverBookResponse.NaverItem item : items) {
			BooleanBuilder inner = new BooleanBuilder();
			inner.and(book.title.contains(cleanHtml(item.getTitle())))
				.and(book.author.contains(cleanHtml(item.getAuthor())));
			outer.or(inner);
		}
		List<Book> dbCandidates = queryFactory.selectFrom(book).where(outer).fetch();

		// 자바에서 normalizedKey 기준으로 완벽 매칭 필터링
		Set<String> incomingKeys = items.stream()
			.map(i -> normalizedKey(i.getTitle(), i.getAuthor()))
			.collect(Collectors.toSet());

		return dbCandidates.stream()
			.filter(b -> incomingKeys.contains(normalizedKey(b.getTitle(), b.getAuthor())))
			.toList();*/

		// 1. Java 측에서 normalizedKey 미리 생성
		Set<String> incomingKeys = items.stream()
			.map(i -> normalizedKey(i.getTitle(), i.getAuthor()))
			.collect(Collectors.toSet());

		// 2. DB에서 전체 후보군 조회 (최적화를 위해 최근 N일 이내, 혹은 전체가 아닌 일부만 조회 가능)
		List<Book> dbCandidates = queryFactory
			.selectFrom(book)
			.fetch();

		// 3. Java에서 normalizedKey로 완전 일치하는 것만 필터링
		return dbCandidates.stream()
			.filter(b -> incomingKeys.contains(normalizedKey(b.getTitle(), b.getAuthor())))
			.toList();
	}

	@Override
	public List<Book> findExistingAladinBooks(List<AladinItemResponse.AladinItem> items) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QBook book = QBook.book;
		BooleanBuilder outer = new BooleanBuilder();

		for (AladinItemResponse.AladinItem item : items) {
			BooleanBuilder inner = new BooleanBuilder();
			inner.and(book.title.eq(normalize(item.getTitle())))
				.and(book.author.eq(normalize(item.getAuthor())));
			outer.or(inner);
		}
		return queryFactory.selectFrom(book).where(outer).fetch();
	}

	private String normalize(String input){
		if(input==null) return "";
		input = input.trim();
		return input;
	}

	private String cleanHtml(String str) {
		if (str == null) return "";
		return str.replaceAll("<[^>]+>", "") // 태그 제거
			.replaceAll("&#?[a-zA-Z0-9]+;", "")
			.replaceAll("\\s+", " ") // 공백 정리
			.trim();
	}

	private String normalizedKey(String title, String author) {
		return cleanHtml(title).replaceAll("\\s+", "").replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase()
			+ "|" +
			cleanHtml(author).replaceAll("\\s+", "").replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase();
	}
}
