package com.example.plusproject.domain.book.entity;

import com.example.plusproject.domain.book.dto.BookRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String author;
    private String publisher;

    @Column(length = 2000)
    private String description;

    private LocalDate publishedAt;

    private int price;
    private int stock;

    private String writerName;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Book(String title, String author, String publisher, String description,
                LocalDate publishedAt, int price, int stock,
                String writerName, String password) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.publishedAt = publishedAt;
        this.price = price;
        this.stock = stock;
        this.writerName = writerName;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 수정용 메서드
    public void update(BookRequestDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
        this.updatedAt = LocalDateTime.now();
    }
}
