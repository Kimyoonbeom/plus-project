package com.example.plusproject.domain.book.controller;

import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 도서 등록
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.createBook(dto));
    }

    // 도서 단건 조회
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> get(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBook(bookId));
    }

    // 도서 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody BookRequestDto dto) {
        bookService.updateBook(id, dto);
        return ResponseEntity.ok(Map.of("message", "수정이 완료되었습니다.", "bookId", id));
    }

    // 도서 삭제 (비밀번호는 요청 바디로 전달)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody String password) {
        bookService.deleteBook(id, password);
        return ResponseEntity.ok().build();
    }

    // 도서 검색
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }
}
