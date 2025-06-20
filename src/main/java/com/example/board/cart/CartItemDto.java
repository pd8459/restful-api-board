package com.example.board.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private int quantity;
    private int price;
    private int totalPrice;
    private String imageUrl;

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.itemId = cartItem.getItem().getId();
        this.itemName = cartItem.getItem().getName();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getItem().getPrice();
        this.totalPrice = this.price * this.quantity;
        this.imageUrl = cartItem.getItem().getImageUrl();
    }
}
