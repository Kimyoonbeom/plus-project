package com.example.plusproject.domain.order.dto.request;

import com.example.plusproject.domain.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusRequest {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }
}
