package com.example.board.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderItemDto {
    private Long itemId;
    private String itemName;
    private int quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.itemId = orderItem.getItem().getId();
        this.itemName = orderItem.getItem().getName();
        this.quantity = orderItem.getQuantity();
    }
}
