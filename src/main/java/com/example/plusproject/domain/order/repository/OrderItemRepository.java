package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 주문상세(OrderItem)만 별도로 CRUD할 일이 있을 때
 * 관리자 페이지 등에서 주문상세만 따로 조회/검색/통계할 때
 * 특정 주문상세(OrderItem)만 직접 조작할 때 사용.
 */

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}
