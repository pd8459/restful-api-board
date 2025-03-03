package com.example.board.payment;

import com.example.board.User.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String imp_uid;  // 아임포트 결제 고유 ID
    private String merchant_uid;  // 가맹점 주문 ID
    private int amount;  // 결제 금액
    private String status;  // 결제 상태 (paid, failed 등)
    private User user;  // 결제한 사용자 객체 추가
}
