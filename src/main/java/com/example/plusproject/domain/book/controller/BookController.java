package com.example.plusproject.domain.book.controller;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.domain.book.dto.BookResponseDto;
import com.example.plusproject.domain.book.service.AladinBookImportService;
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

    private final AladinBookImportService aladinBookImportService;

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

    @PostMapping("/savebookfromaladin")
    public ResponseEntity<String> saveBooksFromAladin(){
        // List<String> keywords = List.of(
        //     "자바", "파이썬", "리액트", "AI", "알고리즘", "컴퓨터", "웹", "백엔드", "프론트엔드",
        //     "데이터베이스", "딥러닝", "머신러닝", "코틀린", "스프링", "HTML", "CSS", "JavaScript", "Node", "Docker",
        //     "쿠버네티스", "DevOps", "클린코드", "JPA", "SQL", "데이터분석", "프로그래밍", "C언어", "C++", "운영체제",
        //     "리눅스", "네트워크", "정보보안", "인공지능", "디자인패턴", "토익", "토플", "일본어", "중국어", "영어회화",
        //     "자기계발", "리더십", "경제", "경영", "마케팅", "브랜딩", "심리학", "역사", "소설", "에세이"
        // );
        // List<String> keywords = List.of("여행","투자","철학");
        // List<String> keywords = List.of("정치","주식","취미");
        List<String> keywords = List.of("사랑","평화","믿음");
        aladinBookImportService.importBooksFromAladin(keywords);
        return ResponseEntity.ok("저장 완료");
    }
}
