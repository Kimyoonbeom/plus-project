package com.example.plusproject.domain.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plusproject.domain.book.entity.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingOrAuthorContaining(String t, String a);
}