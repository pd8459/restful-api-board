package com.example.board.order;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {
    private Long id;
    private int totalAmount;
    private OrderStatus status;

    public OrderDto(Long id, int totalAmount, OrderStatus status) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
    }

}

