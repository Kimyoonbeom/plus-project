package com.example.plusproject.domain.book.repository;

import com.example.plusproject.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 검색 기능 메서드 선언
    List<Book> findByTitleContainingOrAuthorContaining(String title, String author);

    // 예외 처리용 메서드
    default Book findByIdOrElseThrow(Long bookId) {
        return findById(bookId).orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
    }
}
