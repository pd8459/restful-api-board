package com.example.board.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentValidationResponse {
    private boolean success;
    private String message;
    private Long orderId;
}
