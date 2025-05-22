package com.example.plusproject.domain.book.dto;

import com.example.plusproject.domain.book.entity.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookResponseDto {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private int price;
    private int stock;
    private String imageUrl;
    private double rating;


    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.stock = book.getStock();
        this.rating = book.getRating();
    }

    public BookResponseDto(Long id, String title, String author, String publisher,
        String description, Integer price, Integer stock,
        String imageUrl, Double rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }
}
