package com.example.plusproject.domain.book.service;

import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.enums.UserRole;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Long createBook(BookRequestDto dto, User user) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .description(dto.getDescription())
                .publishedAt(dto.getPublishedAt())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .rating(null)  // 최초에는 null
                .build();

        return bookRepository.save(book).getId();
    }

    public BookResponseDto getBook(Long bookId) {
        return new BookResponseDto(getBookEntity(bookId));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "bookSearchLocal", allEntries = true, cacheManager = "localCacheManager"),
        @CacheEvict(value = "bookSearchRedis", allEntries = true, cacheManager = "redisCacheManager"),
    })
    public void updateBook(Long bookId, BookRequestDto dto, User user) {
        Book book = getBookEntity(bookId);

        if(!user.getUserRole().equals(UserRole.ADMIN)){
            throw new SecurityException("수정 권한이 없습니다.");
        }

        book.update(dto);
    }

    @Caching(evict = {
        @CacheEvict(value = "bookSearchLocal", allEntries = true, cacheManager = "localCacheManager"),
        @CacheEvict(value = "bookSearchRedis", allEntries = true, cacheManager = "redisCacheManager"),
    })
    public void deleteBook(Long bookId, User user) {
        Book book = getBookEntity(bookId);

        if(!user.getUserRole().equals(UserRole.ADMIN)){
            throw new SecurityException("수정 권한이 없습니다.");
        }

        bookRepository.delete(book);
    }

    public List<BookResponseDto> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword)
                .stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());
    }


    private Book getBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("책이 존재하지 않습니다."));
    }

    /**
     * 재고 감소 로직
     * 동시성 제어 - 낙관적 락 방법
     * @param id
     */
    @Transactional
    public void decreaseStockWithOptimisticLock(Long id) {
        Book findbook = bookRepository.findByIdOrElseThrow(id);
        findbook.decreaseStock();
        bookRepository.save(findbook);
    }
}
