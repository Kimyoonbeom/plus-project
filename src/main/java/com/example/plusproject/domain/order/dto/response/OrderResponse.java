package com.example.plusproject.domain.order.dto.response;

import com.example.plusproject.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    public static class OrderItemResponse {
        private Long id;
        private Long bookId;
        private String bookTitle;
        private Integer quantity;
        private long price;
    }
}
