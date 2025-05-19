package com.example.plusproject.domain.book.dto;

import com.example.plusproject.domain.book.entity.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private final Long id;
    private final String title;
    private final String author;
    private final String publisher;
    private final String description;
    private final int price;
    private final int stock;
    private final double rating;

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
}
