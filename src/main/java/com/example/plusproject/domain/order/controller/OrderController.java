package com.example.plusproject.domain.order.controller;

import com.example.plusproject.common.dto.AuthUser;
import com.example.plusproject.domain.book.entity.Book;
import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.order.dto.request.OrderCreateRequest;
import com.example.plusproject.domain.order.dto.request.OrderUpdateRequest;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.entity.OrderItem;
import com.example.plusproject.domain.order.service.OrderService;
import com.example.plusproject.domain.user.repository.UserRepository;

import com.example.plusproject.domain.user.entity.User;
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
    private final UserRepository userRepository;

    // 주문 생성 (로그인 유저만)
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody OrderCreateRequest request
    ) {
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

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
                user,
                request.getStatus(),
                items
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
