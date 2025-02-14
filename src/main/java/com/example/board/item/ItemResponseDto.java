package com.example.board.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;

    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.stock = item.getStock();
    }
}
