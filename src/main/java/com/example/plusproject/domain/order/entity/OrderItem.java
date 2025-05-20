package com.example.plusproject.domain.order.entity;

import com.example.plusproject.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private long price;

    @Builder
    public OrderItem(Book book, Integer quantity, long price) {
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null && !order.getOrderItems().contains(this)) {
            order.getOrderItems().add(this);
        }
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

