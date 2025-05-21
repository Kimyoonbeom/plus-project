package com.example.plusproject.domain.book.controller;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.service.BookService;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserRepository userRepository;

    // 도서 등록
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody BookRequestDto dto,
                                       @AuthenticationPrincipal AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return ResponseEntity.ok(bookService.createBook(dto, user));
    }

     // 도서 단건 조회
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> get(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBook(bookId));
    }

    // 도서 수정
    @PutMapping("/{bookId}")
    public ResponseEntity<Void> update(@PathVariable Long bookId,
                                       @RequestBody BookRequestDto dto,
                                       @AuthenticationPrincipal AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        bookService.updateBook(bookId, dto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable Long bookId,
                                       @AuthenticationPrincipal AuthUser authUser) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        bookService.deleteBook(bookId, user);
        return ResponseEntity.ok().build();
    }

    // 도서 검색
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }
}
