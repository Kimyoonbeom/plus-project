package com.example.plusproject.domain.book.entity;

import com.example.plusproject.domain.book.dto.AladinItemResponse;
import com.example.plusproject.domain.book.dto.BookRequestDto;
import com.example.plusproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(nullable = false)
    private Double rating = 0.0;

    public void update(BookRequestDto dto) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
    }

    public void updateRating(double averageRating) {
        this.rating = averageRating;
    }

}
