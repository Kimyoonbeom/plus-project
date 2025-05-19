package com.example.plusproject.domain.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookRequestDto {
    private String title;
    private String author;
    private String publisher;
    private String description;
    private LocalDate publishedAt;

    private int price;
    private int stock;

    private String writerName;
    private String password;
}
