package com.example.board.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String imp_uid;
    private String merchant_uid;
    private int amount;
}