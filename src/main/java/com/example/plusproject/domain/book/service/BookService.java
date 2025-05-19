package com.example.plusproject.domain.book.service;

import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.exception.InvalidPasswordException;
import com.example.plusproject.domain.book.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;  // DB 접근

    //도서 등록 (비회원 가능, 비밀번호 평문 저장)
    public Long createBook(BookRequestDto dto) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .description(dto.getDescription())
                .publishedAt(dto.getPublishedAt())
                .writerName(dto.getWriterName())
                .password(dto.getPassword())  // 비밀번호 평문 저장
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();

        return bookRepository.save(book).getId();
    }

    //도서 단건 조회
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("책이 없습니다"));
        return new BookResponseDto(book);
    }

    //도서 수정
    @Transactional
    public void updateBook(Long id, BookRequestDto dto) {
        Book book = getBookEntity(id);
        validatePassword(dto.getPassword(), book.getPassword());
        book.update(dto);
    }


    //도서 삭제
    public void deleteBook(Long id, String password) {
        Book book = getBookEntity(id);
        validatePassword(password, book.getPassword());
        bookRepository.delete(book);
    }


    // 제목 or 저자 기준 LIKE 검색
    public List<BookResponseDto> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword)
                .stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());
    }

    //ID 기준 도서 조회 (예외 처리 포함)
    private Book getBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("책 없음"));
    }

    //비밀번호 평문 비교
    private void validatePassword(String input, String saved) {
        if (!input.equals(saved)) {
            throw new InvalidPasswordException();
        }
    }
}
