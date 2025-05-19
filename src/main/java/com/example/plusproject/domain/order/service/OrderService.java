package com.example.plusproject.domain.order.service;

import com.example.plusproject.domain.book.repository.BookRepository;
import com.example.plusproject.domain.order.dto.request.OrderItemUpdateRequest;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.entity.OrderItem;
import com.example.plusproject.domain.order.entity.OrderStatus;
import com.example.plusproject.domain.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    // 주문 생성
    @Transactional
    public Order createOrder(Long userId, OrderStatus status, List<OrderItem> items) {
        Order order = new Order(userId, status);
        order.addOrderItems(items);
        return orderRepository.save(order);
    }

    // 주문 단건 조회
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
    }

    // 유저별 주문 전체 조회
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // 주문 상태 + 주문상세(수량 등)의 수정.
    @Transactional
    public Order updateOrderAndItems(Long orderId, OrderStatus status, List<OrderItemUpdateRequest> itemRequests) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        if (status != null) {
            order.changeStatus(status);
        }

        if (itemRequests != null) {
            for (OrderItemUpdateRequest req : itemRequests) {
                OrderItem item = order.getOrderItems().stream()
                        .filter(i -> i.getId().equals(req.getOrderItemId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("주문상세를 찾을 수 없습니다."));
                if (req.getQuantity() != null) {
                    item.setQuantity(req.getQuantity());
                }
            }
        }
        return order;
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
