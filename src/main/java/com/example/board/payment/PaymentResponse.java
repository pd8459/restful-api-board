package com.example.board.payment;

import com.example.board.User.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String imp_uid;
    private String merchant_uid;
    private int amount;
    private String status;
    private User user;
}
