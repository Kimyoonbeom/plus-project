package com.example.plusproject.domain.order.entity;

import com.example.plusproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 상태 변경 전용 생성자
    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public Order(Long userId, OrderStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public void addOrderItems(List<OrderItem> items) {
        for (OrderItem item : items) {
            item.setOrder(this);
        }
        this.orderItems.addAll(items);
    }
}
