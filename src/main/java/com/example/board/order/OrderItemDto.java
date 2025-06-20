package com.example.board.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long itemId;
    private String itemName;
    private int quantity;
    private int price;

    public static OrderItemDto from(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setItemId(orderItem.getItem().getId());
        dto.setItemName(orderItem.getItem().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        return dto;
    }
}
