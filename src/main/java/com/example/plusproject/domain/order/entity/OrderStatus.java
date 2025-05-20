package com.example.plusproject.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    WAITING,    // 대기
    COMPLETED,  // 완료
    CANCELED    // 취소
}
