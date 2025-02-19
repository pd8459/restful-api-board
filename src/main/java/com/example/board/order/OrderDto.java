package com.example.board.order;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {
    private Long orderId;
    private String status;
    private List<OrderItemDto> items;

    public OrderDto(Order order, List<OrderItem> orderItems) {
        this.orderId = order.getId();
        this.status = order.getStatus().name();
        this.items = orderItems.stream().map(OrderItemDto::new).collect(Collectors.toList());
    }
}
