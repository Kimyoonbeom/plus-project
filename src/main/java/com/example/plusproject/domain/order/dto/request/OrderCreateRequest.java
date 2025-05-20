package com.example.plusproject.domain.order.dto.request;

import com.example.plusproject.domain.order.entity.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    private OrderStatus status;
    private List<OrderItemRequest> items;

    @Getter
    @NoArgsConstructor
    public static class OrderItemRequest {
        private Long bookId;
        private Integer quantity;
    }
}
