package com.example.plusproject.domain.order.service;

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

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        order.changeStatus(status);

        return orderRepository.save(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
