package com.example.board.order;

public enum OrderStatus {
    PENDING,   // 결제 대기 중
    SUCCESS,   // 결제 성공
    FAILED,    // 결제 실패
    CANCELLED  // 결제 취소
}
