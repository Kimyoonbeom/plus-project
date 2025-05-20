package com.example.plusproject.domain.order.controller;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.order.dto.request.OrderCreateRequest;
import com.example.plusproject.domain.order.dto.request.OrderUpdateRequest;
import com.example.plusproject.domain.order.dto.response.OrderResponse;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.entity.OrderItem;
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
    private final BookRepository bookRepository;

    // 주문 생성 (로그인 유저만)
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody OrderCreateRequest request
    ) {
        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> {
                    Book book = bookRepository.findById(itemReq.getBookId())
                            .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
                    return OrderItem.builder()
                            .book(book)
                            .quantity(itemReq.getQuantity())
                            .price(book.getPrice())
                            .build();
                }).toList();

        Order order = orderService.createOrder(
                authUser.getId(),
                request.getStatus(),
                items
        );
        // 엔티티 → DTO 변환
        OrderResponse response = orderService.toOrderResponse(order);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 주문 단건 조회
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    // 유저의 주문 전체 조회(v1: 캐시 미적용)
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // 유저별 주문 전체 조회 (v2: 캐시 적용, DTO 반환)
    @GetMapping("/v2/users/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrdersWithCache(@PathVariable Long userId) {
        List<OrderResponse> responses = orderService.getOrdersByUserWithCache(userId);
        return ResponseEntity.ok(responses);
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
