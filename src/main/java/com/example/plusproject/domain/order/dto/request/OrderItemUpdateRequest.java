package com.example.plusproject.domain.order.dto.request;

import lombok.Getter;

// 주문 상태 + 주문상세(수량 등)도 같이 바꾸고 싶을 때
@Getter
public class OrderItemUpdateRequest {
    private Long orderItemId;
    private Integer quantity;
}
