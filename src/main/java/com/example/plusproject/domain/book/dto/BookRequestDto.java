package com.example.plusproject.domain.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BookRequestDto {

    private String title;
    private String author;
    private String publisher;
    private String description;
    private LocalDate publishedAt;
    private int price;
    private int stock;
}
