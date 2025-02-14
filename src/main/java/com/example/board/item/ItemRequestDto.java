package com.example.board.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequestDto {
    private String name;
    private String description;
    private int price;
    private int stock;
}
