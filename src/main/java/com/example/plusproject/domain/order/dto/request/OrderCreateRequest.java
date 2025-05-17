package com.example.plusproject.domain.order.dto.request;

import com.example.plusproject.domain.order.entity.OrderItem;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    private String status;
    private List<OrderItem> items;
}
