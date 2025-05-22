package com.example.plusproject.domain.book.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.plusproject.domain.book.dto.BookResponseDto;
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
		if(keyword!=null){
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
				book.rating))
			.from(book)
			.where(builder)
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
}
