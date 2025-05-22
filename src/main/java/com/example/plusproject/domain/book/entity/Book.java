package com.example.plusproject.domain.book.entity;

import com.example.plusproject.domain.book.dto.AladinItemResponse;
import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String publisher;

    @Column(length = 2000)
    private String description;

    private LocalDate publishedAt;
    private int price;
    private int stock;

    @Column
    private Double rating;  // 리뷰가 없으면 null 가능

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Book(String title, String author, String publisher, String description,
                LocalDate publishedAt, int price, int stock, User user, Double rating) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.publishedAt = publishedAt;
        this.price = price;
        this.stock = stock;
        this.user = user;
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

}
