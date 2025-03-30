package com.example.board.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    private String email;
    private Long itemId;
    private int quantity;
}
