package com.example.plusproject.domain.book.dto;

import com.example.plusproject.domain.book.entity.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private int price;
    private int stock;

    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.stock = book.getStock();
    }
}
