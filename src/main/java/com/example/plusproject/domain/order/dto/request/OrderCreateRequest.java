package com.example.plusproject.domain.order.dto.request;

import com.example.plusproject.domain.order.entity.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    private OrderStatus status;
    private List<OrderItemRequest> items;

    @Getter
    public static class OrderItemRequest {
        private Long bookId;
        private Integer quantity;
    }
}
