package com.example.plusproject.domain.book.entity;

import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book", indexes = {
    @Index(name = "idx_book_title_author", columnList = "title, author")
})
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String publisher;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate publishedAt;
    private int price;
    private int stock;
    private String imageUrl;

    @Column
    private Double rating;  // 리뷰가 없으면 null 가능


    /**
     * 동시성 제어 -  낙관적 락 방법
     */
    @Version
    private int version;

    @Builder
    public Book(String title, String author, String publisher, String description,
                LocalDate publishedAt, int price, int stock, String imageUrl, Double rating) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.publishedAt = publishedAt;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public void update(BookRequestDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
    }

    public void updateRating(Double rating) {
        this.rating = rating;
    }

    /**
     * 재고 감소 로직
     * 재고가 0이라면 예외처리
     */
    public void decreaseStock() {
        if(stock <= 0) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        this.stock -= 1;
    }
}
