package com.example.plusproject.domain.order.controller;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.order.dto.request.OrderCreateRequest;
import com.example.plusproject.domain.order.dto.request.OrderUpdateRequest;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 생성 (로그인 유저만)
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody OrderCreateRequest request
    ) {
        Order order = orderService.createOrder(
                authUser.getId(),
                request.getStatus(),
                request.getItems()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // 주문 단건 조회
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    // 유저의 주문 전체 조회
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // 특정 유저의 특정 주문 조회
    @GetMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<Order> getUserOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId
    ) {
        // orderId로 주문을 찾고, userId가 일치 하는지 검증
        Order order = orderService.getOrder(orderId);
        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(order);
    }

    // 주문+주문상세 복합 변경
    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<Order> updateOrderAndItems(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request
    ) {
        Order updatedOrder = orderService.updateOrderAndItems(
                orderId,
                request.getStatus(),
                request.getItems()
        );
        return ResponseEntity.ok(updatedOrder);
    }


    // 주문 삭제
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
