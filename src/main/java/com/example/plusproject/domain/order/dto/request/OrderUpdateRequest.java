package com.example.plusproject.domain.order.dto.request;

import com.example.plusproject.domain.order.entity.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 주문+주문상세 복합 변경용 DTO
@Getter
@NoArgsConstructor
public class OrderUpdateRequest {
    private OrderStatus status; // 주문 상태
    private List<OrderItemUpdateRequest> items; // 주문상세 리스트
}
