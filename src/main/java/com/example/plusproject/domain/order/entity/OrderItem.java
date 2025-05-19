package com.example.plusproject.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long price;

    public OrderItem(Long bookId, int quantity, long price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }
}

